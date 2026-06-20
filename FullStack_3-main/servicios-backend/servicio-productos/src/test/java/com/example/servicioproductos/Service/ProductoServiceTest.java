package com.example.servicioproductos.Service;

import com.example.servicioproductos.Model.Producto;
import com.example.servicioproductos.Repository.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setVendedorId(100L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción del producto");
        producto.setPrecio(99.99);
        producto.setStock(50);
        producto.setImagenUrl("http://example.com/imagen.jpg");
        producto.setCategoria("Electrónica");
        producto.setActivo(true);
    }

    @Test
    void listar_ProductosActivos() {
        List<Producto> productosActivos = Arrays.asList(producto);
        when(productoRepository.findByActivoTrue()).thenReturn(productosActivos);

        List<Producto> result = productoService.listar();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Producto Test", result.get(0).getNombre());
        verify(productoRepository, times(1)).findByActivoTrue();
    }

    @Test
    void listarPorVendedor_ProductosDeVendedor() {
        List<Producto> productosVendedor = Arrays.asList(producto);
        when(productoRepository.findByVendedorId(100L)).thenReturn(productosVendedor);

        List<Producto> result = productoService.listarPorVendedor(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getVendedorId());
        verify(productoRepository, times(1)).findByVendedorId(100L);
    }

    @Test
    void guardar_Exito() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.guardar(producto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Producto Test", result.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(kafkaTemplate, times(1)).send(eq("topic-stock-productos"), any(String.class));
    }

    @Test
    void guardar_PrecioNegativo() {
        producto.setPrecio(-10.0);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.guardar(producto);
        });

        assertEquals("El precio no puede ser negativo", exception.getMessage());
        verify(productoRepository, never()).save(any(Producto.class));
        verify(kafkaTemplate, never()).send(any(String.class), any(String.class));
    }

    @Test
    void guardar_StockNegativo() {
        producto.setStock(-5);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.guardar(producto);
        });

        assertEquals("El stock no puede ser negativo", exception.getMessage());
        verify(productoRepository, never()).save(any(Producto.class));
        verify(kafkaTemplate, never()).send(any(String.class), any(String.class));
    }

    @Test
    void guardar_ActivoNull_SeteaTrue() {
        producto.setActivo(null);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.guardar(producto);

        assertNotNull(result);
        assertTrue(result.getActivo());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void obtenerPorId_Exito() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto result = productoService.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Producto Test", result.getNombre());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorId_NoEncontrado() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.obtenerPorId(999L);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productoRepository, times(1)).findById(999L);
    }

    @Test
    void eliminar_SoftDelete() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        productoService.eliminar(1L);

        assertFalse(producto.getActivo());
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void cambiarEstadoActivo_Exito() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.cambiarEstadoActivo(1L, false);

        assertNotNull(result);
        assertFalse(result.getActivo());
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void restarStock_Exito() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.restarStock(1L, 10);

        assertNotNull(result);
        assertEquals(40, result.getStock());
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(kafkaTemplate, times(1)).send(eq("topic-stock-productos"), any(String.class));
    }

    @Test
    void restarStock_StockInsuficiente() {
        producto.setStock(5);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.restarStock(1L, 10);
        });

        assertTrue(exception.getMessage().contains("Stock insuficiente"));
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, never()).save(any(Producto.class));
        verify(kafkaTemplate, never()).send(any(String.class), any(String.class));
    }

    @Test
    void restarStock_StockExacto() {
        producto.setStock(10);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.restarStock(1L, 10);

        assertNotNull(result);
        assertEquals(0, result.getStock());
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }
}
