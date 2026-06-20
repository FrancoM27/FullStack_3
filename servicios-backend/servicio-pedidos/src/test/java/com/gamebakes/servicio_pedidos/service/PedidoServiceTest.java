package com.gamebakes.servicio_pedidos.service;

import com.gamebakes.servicio_pedidos.model.Pedido;
import com.gamebakes.servicio_pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedidoTest;

    // SETUP: Preparación del entorno y datos de prueba
    @BeforeEach
    void setUp() {
        pedidoTest = new Pedido();
        pedidoTest.setId(1L);
        pedidoTest.setClienteId(1L);
        pedidoTest.setClienteNombre("Cliente 1");
        pedidoTest.setProductoId(1L);
        pedidoTest.setProductoNombre("Producto 1");
        pedidoTest.setVendedorId(1L);
        pedidoTest.setEstado("PENDIENTE");
        pedidoTest.setCantidad(2);
    }

    // EXECUTION + VERIFICATION: Ejecución y comparación del resultado
    @Test
    void testObtenerPedidosPorCliente() {
        // SETUP
        List<Pedido> pedidosEsperados = Arrays.asList(pedidoTest);
        when(pedidoRepository.findByClienteId(1L)).thenReturn(pedidosEsperados);

        // EXECUTION
        List<Pedido> resultado = pedidoService.obtenerPedidosPorCliente(1L);

        // VERIFICATION
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getClienteId());
        assertEquals("Cliente 1", resultado.get(0).getClienteNombre());
        verify(pedidoRepository, times(1)).findByClienteId(1L);
    }

    @Test
    void testObtenerPedidosPorVendedor() {
        // SETUP
        List<Pedido> pedidosEsperados = Arrays.asList(pedidoTest);
        when(pedidoRepository.findByVendedorId(1L)).thenReturn(pedidosEsperados);

        // EXECUTION
        List<Pedido> resultado = pedidoService.obtenerPedidosPorVendedor(1L);

        // VERIFICATION
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getVendedorId());
        verify(pedidoRepository, times(1)).findByVendedorId(1L);
    }

    @Test
    void testCrearPedido() {
        // SETUP
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoTest);

        // EXECUTION
        Pedido resultado = pedidoService.crearPedido(pedidoTest);

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals(1L, resultado.getId());
        verify(pedidoRepository, times(1)).save(pedidoTest);
        verify(kafkaTemplate, times(1)).send(eq("seguimiento-pedidos"), anyString());
    }

    @Test
    void testActualizarEstado() {
        // SETUP
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoTest));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoTest);

        // EXECUTION
        Pedido resultado = pedidoService.actualizarEstado(1L, "EN_CAMINO");

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals("EN_CAMINO", resultado.getEstado());
        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, times(1)).save(pedidoTest);
        verify(kafkaTemplate, times(1)).send(eq("seguimiento-pedidos"), anyString());
    }

    @Test
    void testActualizarEstado_NoEncontrado() {
        // SETUP
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        // EXECUTION + VERIFICATION
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.actualizarEstado(1L, "EN_CAMINO");
        });

        assertEquals("Pedido no encontrado", exception.getMessage());
        verify(pedidoRepository, times(1)).findById(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    @Test
    void testValidarCompra_ConPedidos() {
        // SETUP
        List<Pedido> pedidos = Arrays.asList(pedidoTest);
        when(pedidoRepository.findByClienteIdAndProductoId(1L, 1L)).thenReturn(pedidos);

        // EXECUTION
        boolean resultado = pedidoService.validarCompra(1L, 1L);

        // VERIFICATION
        assertTrue(resultado);
        verify(pedidoRepository, times(1)).findByClienteIdAndProductoId(1L, 1L);
    }

    @Test
    void testValidarCompra_SinPedidos() {
        // SETUP
        when(pedidoRepository.findByClienteIdAndProductoId(1L, 1L)).thenReturn(Arrays.asList());

        // EXECUTION
        boolean resultado = pedidoService.validarCompra(1L, 1L);

        // VERIFICATION
        assertFalse(resultado);
        verify(pedidoRepository, times(1)).findByClienteIdAndProductoId(1L, 1L);
    }

    @Test
    void testValidarEntregado_ConEstadoEntregado() {
        // SETUP
        pedidoTest.setEstado("ENTREGADO");
        List<Pedido> pedidos = Arrays.asList(pedidoTest);
        when(pedidoRepository.findByClienteIdAndProductoId(1L, 1L)).thenReturn(pedidos);

        // EXECUTION
        boolean resultado = pedidoService.validarEntregado(1L, 1L);

        // VERIFICATION
        assertTrue(resultado);
        verify(pedidoRepository, times(1)).findByClienteIdAndProductoId(1L, 1L);
    }

    @Test
    void testValidarEntregado_SinEstadoEntregado() {
        // SETUP
        pedidoTest.setEstado("PENDIENTE");
        List<Pedido> pedidos = Arrays.asList(pedidoTest);
        when(pedidoRepository.findByClienteIdAndProductoId(1L, 1L)).thenReturn(pedidos);

        // EXECUTION
        boolean resultado = pedidoService.validarEntregado(1L, 1L);

        // VERIFICATION
        assertFalse(resultado);
        verify(pedidoRepository, times(1)).findByClienteIdAndProductoId(1L, 1L);
    }

    @Test
    void testValidarEntregado_MultiplesPedidos_UnoEntregado() {
        // SETUP
        Pedido pedidoPendiente = new Pedido();
        pedidoPendiente.setId(2L);
        pedidoPendiente.setEstado("PENDIENTE");

        Pedido pedidoEntregado = new Pedido();
        pedidoEntregado.setId(3L);
        pedidoEntregado.setEstado("ENTREGADO");

        List<Pedido> pedidos = Arrays.asList(pedidoPendiente, pedidoEntregado);
        when(pedidoRepository.findByClienteIdAndProductoId(1L, 1L)).thenReturn(pedidos);

        // EXECUTION
        boolean resultado = pedidoService.validarEntregado(1L, 1L);

        // VERIFICATION
        assertTrue(resultado);
        verify(pedidoRepository, times(1)).findByClienteIdAndProductoId(1L, 1L);
    }

    @Test
    void testActualizarEstado_MultiplesCambios() {
        // SETUP
        Pedido pedido1 = new Pedido();
        pedido1.setId(1L);
        pedido1.setEstado("PREPARACION");

        Pedido pedido2 = new Pedido();
        pedido2.setId(1L);
        pedido2.setEstado("EN_CAMINO");

        Pedido pedido3 = new Pedido();
        pedido3.setId(1L);
        pedido3.setEstado("ENTREGADO");

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoTest));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido1).thenReturn(pedido2).thenReturn(pedido3);

        // EXECUTION
        Pedido resultado1 = pedidoService.actualizarEstado(1L, "PREPARACION");
        Pedido resultado2 = pedidoService.actualizarEstado(1L, "EN_CAMINO");
        Pedido resultado3 = pedidoService.actualizarEstado(1L, "ENTREGADO");

        // VERIFICATION
        assertEquals("PREPARACION", resultado1.getEstado());
        assertEquals("EN_CAMINO", resultado2.getEstado());
        assertEquals("ENTREGADO", resultado3.getEstado());
        verify(pedidoRepository, times(3)).findById(1L);
        verify(pedidoRepository, times(3)).save(any(Pedido.class));
        verify(kafkaTemplate, times(3)).send(eq("seguimiento-pedidos"), anyString());
    }

    @Test
    void testObtenerPedidosPorCliente_Vacio() {
        // SETUP
        when(pedidoRepository.findByClienteId(1L)).thenReturn(Arrays.asList());

        // EXECUTION
        List<Pedido> resultado = pedidoService.obtenerPedidosPorCliente(1L);

        // VERIFICATION
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(pedidoRepository, times(1)).findByClienteId(1L);
    }

    @Test
    void testObtenerPedidosPorVendedor_Vacio() {
        // SETUP
        when(pedidoRepository.findByVendedorId(1L)).thenReturn(Arrays.asList());

        // EXECUTION
        List<Pedido> resultado = pedidoService.obtenerPedidosPorVendedor(1L);

        // VERIFICATION
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(pedidoRepository, times(1)).findByVendedorId(1L);
    }

    // TEARDOWN: Limpieza (automático con JUnit 5)
}
