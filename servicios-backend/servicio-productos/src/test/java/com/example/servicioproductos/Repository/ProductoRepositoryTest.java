package com.example.servicioproductos.Repository;

import com.example.servicioproductos.Model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoRepositoryTest {

    @Mock
    private ProductoRepository productoRepository;

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
    void findByActivoTrue_ProductosActivos() {
        List<Producto> productosActivos = Arrays.asList(producto);
        when(productoRepository.findByActivoTrue()).thenReturn(productosActivos);

        List<Producto> result = productoRepository.findByActivoTrue();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Producto Test", result.get(0).getNombre());
        assertTrue(result.get(0).getActivo());
        verify(productoRepository, times(1)).findByActivoTrue();
    }

    @Test
    void findByActivoTrue_SinProductosActivos() {
        when(productoRepository.findByActivoTrue()).thenReturn(Arrays.asList());

        List<Producto> result = productoRepository.findByActivoTrue();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productoRepository, times(1)).findByActivoTrue();
    }

    @Test
    void findByVendedorId_ProductosDeVendedor() {
        List<Producto> productosVendedor = Arrays.asList(producto);
        when(productoRepository.findByVendedorId(100L)).thenReturn(productosVendedor);

        List<Producto> result = productoRepository.findByVendedorId(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getVendedorId());
        verify(productoRepository, times(1)).findByVendedorId(100L);
    }

    @Test
    void findByVendedorId_VendedorSinProductos() {
        when(productoRepository.findByVendedorId(999L)).thenReturn(Arrays.asList());

        List<Producto> result = productoRepository.findByVendedorId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productoRepository, times(1)).findByVendedorId(999L);
    }

    @Test
    void findByCategoria_ProductosDeCategoria() {
        List<Producto> productosCategoria = Arrays.asList(producto);
        when(productoRepository.findByCategoria("Electrónica")).thenReturn(productosCategoria);

        List<Producto> result = productoRepository.findByCategoria("Electrónica");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electrónica", result.get(0).getCategoria());
        verify(productoRepository, times(1)).findByCategoria("Electrónica");
    }

    @Test
    void findByCategoria_CategoriaSinProductos() {
        when(productoRepository.findByCategoria("Ropa")).thenReturn(Arrays.asList());

        List<Producto> result = productoRepository.findByCategoria("Ropa");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productoRepository, times(1)).findByCategoria("Ropa");
    }

    @Test
    void save_ProductoGuardado() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoRepository.save(producto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Producto Test", result.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void findById_ProductoEncontrado() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> result = productoRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Producto Test", result.get().getNombre());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ProductoNoEncontrado() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Producto> result = productoRepository.findById(999L);

        assertFalse(result.isPresent());
        verify(productoRepository, times(1)).findById(999L);
    }

    @Test
    void deleteById_ProductoEliminado() {
        doNothing().when(productoRepository).deleteById(1L);

        productoRepository.deleteById(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }
}
