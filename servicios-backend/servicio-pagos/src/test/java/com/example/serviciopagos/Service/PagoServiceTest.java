package com.example.serviciopagos.Service;

import com.example.serviciopagos.DTO.SolicitudPagoDTO;
import com.example.serviciopagos.Model.CarritoItem;
import com.example.serviciopagos.Model.Pago;
import com.example.serviciopagos.Model.ProductoStockCache;
import com.example.serviciopagos.Repository.PagoRepository;
import com.example.serviciopagos.Repository.ProductoStockCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private CarritoService carritoService;

    @Mock
    private ProductoStockCacheRepository stockCacheRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private PagoService pagoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void iniciarPagoMP_ProductoNoExiste_LanzaExcepcion() {
        SolicitudPagoDTO solicitud = new SolicitudPagoDTO();
        solicitud.setProductoId(99L);

        when(stockCacheRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pagoService.iniciarPagoMP(solicitud));
        assertEquals("El producto no se encuentra disponible en el catálogo.", exception.getMessage());
    }

    @Test
    void iniciarPagoMP_SinStock_LanzaExcepcion() {
        SolicitudPagoDTO solicitud = new SolicitudPagoDTO();
        solicitud.setProductoId(1L);

        ProductoStockCache stock = new ProductoStockCache();
        stock.setStockDisponible(0);

        when(stockCacheRepository.findById(1L)).thenReturn(Optional.of(stock));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pagoService.iniciarPagoMP(solicitud));
        assertEquals("El producto se encuentra agotado.", exception.getMessage());
    }

    @Test
    void iniciarPagoMP_StockInsuficiente_LanzaExcepcion() {
        SolicitudPagoDTO solicitud = new SolicitudPagoDTO();
        solicitud.setProductoId(1L);
        solicitud.setCantidad(5);

        ProductoStockCache stock = new ProductoStockCache();
        stock.setStockDisponible(2);

        when(stockCacheRepository.findById(1L)).thenReturn(Optional.of(stock));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pagoService.iniciarPagoMP(solicitud));
        assertTrue(exception.getMessage().contains("No hay suficiente stock"));
    }

    @Test
    void iniciarPagoMP_IntentoValido_CaeEnCatchMercadoPago() {
        SolicitudPagoDTO solicitud = new SolicitudPagoDTO();
        solicitud.setProductoId(1L);
        solicitud.setCantidad(1);
        solicitud.setMonto(10000.0);
        solicitud.setClienteId(1L);

        ProductoStockCache stock = new ProductoStockCache();
        stock.setStockDisponible(10);

        when(stockCacheRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThrows(RuntimeException.class, () -> pagoService.iniciarPagoMP(solicitud));
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void iniciarPagoCarrito_CarritoVacio_LanzaExcepcion() {
        Long clienteId = 1L;
        when(carritoService.listarPorCliente(clienteId)).thenReturn(new ArrayList<>());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pagoService.iniciarPagoCarrito(clienteId));
        assertEquals("El carrito está vacío", exception.getMessage());
    }

    @Test
    void iniciarPagoCarrito_StockInsuficienteEnItem_LanzaExcepcion() {
        Long clienteId = 1L;
        CarritoItem item = new CarritoItem();
        item.setProductoId(1L);
        item.setCantidad(5);

        ProductoStockCache stock = new ProductoStockCache();
        stock.setStockDisponible(2);

        when(carritoService.listarPorCliente(clienteId)).thenReturn(List.of(item));
        when(stockCacheRepository.findById(1L)).thenReturn(Optional.of(stock));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pagoService.iniciarPagoCarrito(clienteId));
        assertTrue(exception.getMessage().contains("Stock insuficiente"));
    }

    @Test
    void iniciarPagoCarrito_IntentoValido_CaeEnCatchMercadoPago() {
        Long clienteId = 1L;
        CarritoItem item = new CarritoItem();
        item.setProductoId(1L);
        item.setCantidad(1);
        item.setPrecioUnitario(5000.0);

        ProductoStockCache stock = new ProductoStockCache();
        stock.setStockDisponible(10);

        when(carritoService.listarPorCliente(clienteId)).thenReturn(List.of(item));
        when(stockCacheRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThrows(RuntimeException.class, () -> pagoService.iniciarPagoCarrito(clienteId));
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void confirmarPago_PagoNoEncontrado_LanzaExcepcion() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            pagoService.confirmarPago(99L, 1L, "token", "Luis");
        });
    }

    @Test
    void confirmarPago_NoAutorizado_LanzaExcepcion() {
        Long idPago = 1L;
        Pago pago = new Pago();
        pago.setIdPago(idPago);
        pago.setClienteId(1L);

        when(pagoRepository.findById(idPago)).thenReturn(Optional.of(pago));

        assertThrows(RuntimeException.class, () -> pagoService.confirmarPago(idPago, 2L, "token", "Ladron"));
    }

    @Test
    void confirmarPago_YaAprobado_RetornaSinHacerNada() {
        Pago pago = new Pago();
        pago.setClienteId(1L);
        pago.setEstado("APROBADO");

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        Pago resultado = pagoService.confirmarPago(1L, 1L, "token", "Luis");

        assertEquals("APROBADO", resultado.getEstado());
        verify(carritoService, times(1)).limpiarCarrito(1L);
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void confirmarPago_Pendiente_ExitoTotal() {
        Pago pago = new Pago();
        pago.setClienteId(1L);
        pago.setEstado("PENDIENTE");
        pago.setProductoId(10L);
        pago.setCantidad(2);
        pago.setMonto(20000.0);

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

        Pago resultado = pagoService.confirmarPago(1L, 1L, "token", "Luis");

        assertEquals("APROBADO", resultado.getEstado());
        verify(kafkaTemplate, times(1)).send(eq("pago-exitoso-topic"), anyString());
        verify(carritoService, times(1)).limpiarCarrito(1L);
    }

    @Test
    void confirmarPago_DeCarrito_ExitoTotal() {
        Pago pago = new Pago();
        pago.setClienteId(1L);
        pago.setEstado("PENDIENTE");
        pago.setProductoId(null);
        pago.setCantidad(null);
        pago.setMonto(20000.0);

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

        CarritoItem item = new CarritoItem();
        item.setProductoId(5L);
        item.setCantidad(2);
        when(carritoService.listarPorCliente(1L)).thenReturn(List.of(item));

        Pago resultado = pagoService.confirmarPago(1L, 1L, "token", "Luis");

        assertEquals("APROBADO", resultado.getEstado());
        verify(carritoService, times(1)).listarPorCliente(1L);
        verify(kafkaTemplate, times(1)).send(eq("pago-exitoso-topic"), anyString());
    }

    @Test
    void confirmarPago_FallaAlEnviarKafka_LanzaExcepcion() {
        Pago pago = new Pago();
        pago.setIdPago(1L);
        pago.setClienteId(1L);
        pago.setEstado("PENDIENTE");
        pago.setProductoId(10L);
        pago.setCantidad(2);

        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);
        when(kafkaTemplate.send(anyString(), anyString())).thenThrow(new RuntimeException("Timeout Kafka"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.confirmarPago(1L, 1L, "token", "Luis");
        });
        assertTrue(exception.getMessage().contains("Error al convertir pagoInfo a JSON"));
    }

    @Test
    void obtenerHistorialPorCliente_Exito() {
        Pago pago1 = new Pago();
        Pago pago2 = new Pago();
        when(pagoRepository.findByClienteId(1L)).thenReturn(List.of(pago1, pago2));

        List<Pago> historial = pagoService.obtenerHistorialPorCliente(1L);

        assertEquals(2, historial.size());
        verify(pagoRepository, times(1)).findByClienteId(1L);
    }
}