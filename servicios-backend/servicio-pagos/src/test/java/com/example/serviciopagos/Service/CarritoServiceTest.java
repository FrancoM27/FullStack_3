package com.example.serviciopagos.Service;

import com.example.serviciopagos.Model.CarritoItem;
import com.example.serviciopagos.Model.ProductoStockCache;
import com.example.serviciopagos.Repository.CarritoItemRepository;
import com.example.serviciopagos.Repository.ProductoStockCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarritoServiceTest {

    @Mock
    private CarritoItemRepository carritoItemRepository;

    @Mock
    private ProductoStockCacheRepository stockCacheRepository;

    @InjectMocks
    private CarritoService carritoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarPorCliente_Exito() {
        when(carritoItemRepository.findByClienteId(1L)).thenReturn(List.of(new CarritoItem(), new CarritoItem()));
        List<CarritoItem> resultado = carritoService.listarPorCliente(1L);
        assertEquals(2, resultado.size());
    }

    @Test
    void guardarItem_ProductoNoEncontrado_LanzaExcepcion() {
        CarritoItem item = new CarritoItem();
        item.setProductoId(99L);
        when(stockCacheRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> carritoService.guardarItem(item));
        assertEquals("Producto no encontrado en el catalogo", ex.getMessage());
    }

    @Test
    void guardarItem_ProductoAgotado_LanzaExcepcion() {
        CarritoItem item = new CarritoItem();
        item.setProductoId(1L);

        ProductoStockCache stock = new ProductoStockCache();
        stock.setStockDisponible(0);
        when(stockCacheRepository.findById(1L)).thenReturn(Optional.of(stock));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> carritoService.guardarItem(item));
        assertEquals("Producto agotado", ex.getMessage());
    }

    @Test
    void guardarItem_StockInsuficiente_LanzaExcepcion() {
        CarritoItem item = new CarritoItem();
        item.setProductoId(1L);
        item.setCantidad(5);

        ProductoStockCache stock = new ProductoStockCache();
        stock.setStockDisponible(2); // Menos de los 5 que pide
        when(stockCacheRepository.findById(1L)).thenReturn(Optional.of(stock));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> carritoService.guardarItem(item));
        assertEquals("No hay suficiente Stock", ex.getMessage());
    }

    @Test
    void guardarItem_Exito() {
        CarritoItem item = new CarritoItem();
        item.setProductoId(1L);
        item.setCantidad(2);

        ProductoStockCache stock = new ProductoStockCache();
        stock.setStockDisponible(10);

        when(stockCacheRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(carritoItemRepository.save(item)).thenReturn(item);

        CarritoItem guardado = carritoService.guardarItem(item);
        assertNotNull(guardado);
        verify(carritoItemRepository, times(1)).save(item);
    }

    @Test
    void eliminarItem_Exito() {
        carritoService.eliminarItem(1L);
        verify(carritoItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void limpiarCarrito_Exito() {
        carritoService.limpiarCarrito(1L);
        verify(carritoItemRepository, times(1)).deleteByClienteId(1L);
    }
}