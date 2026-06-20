package com.example.servicioproductos.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
    }

    @Test
    void testGettersAndSetters() {
        producto.setId(1L);
        producto.setVendedorId(100L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción del producto");
        producto.setPrecio(99.99);
        producto.setStock(50);
        producto.setImagenUrl("http://example.com/imagen.jpg");
        producto.setCategoria("Electrónica");
        producto.setActivo(true);

        assertEquals(1L, producto.getId());
        assertEquals(100L, producto.getVendedorId());
        assertEquals("Producto Test", producto.getNombre());
        assertEquals("Descripción del producto", producto.getDescripcion());
        assertEquals(99.99, producto.getPrecio());
        assertEquals(50, producto.getStock());
        assertEquals("http://example.com/imagen.jpg", producto.getImagenUrl());
        assertEquals("Electrónica", producto.getCategoria());
        assertTrue(producto.getActivo());
    }

    @Test
    void testConstructorVacio() {
        Producto productoVacio = new Producto();
        assertNotNull(productoVacio);
    }

    @Test
    void testConstructorCompleto() {
        Producto productoCompleto = new Producto(
            1L,
            100L,
            "Producto Test",
            "Descripción del producto",
            99.99,
            50,
            "http://example.com/imagen.jpg",
            "Electrónica",
            true
        );

        assertEquals(1L, productoCompleto.getId());
        assertEquals(100L, productoCompleto.getVendedorId());
        assertEquals("Producto Test", productoCompleto.getNombre());
        assertEquals("Descripción del producto", productoCompleto.getDescripcion());
        assertEquals(99.99, productoCompleto.getPrecio());
        assertEquals(50, productoCompleto.getStock());
        assertEquals("http://example.com/imagen.jpg", productoCompleto.getImagenUrl());
        assertEquals("Electrónica", productoCompleto.getCategoria());
        assertTrue(productoCompleto.getActivo());
    }

    @Test
    void testCamposNulos() {
        producto.setId(null);
        producto.setVendedorId(null);
        producto.setNombre(null);
        producto.setDescripcion(null);
        producto.setPrecio(null);
        producto.setStock(null);
        producto.setImagenUrl(null);
        producto.setCategoria(null);
        producto.setActivo(null);

        assertNull(producto.getId());
        assertNull(producto.getVendedorId());
        assertNull(producto.getNombre());
        assertNull(producto.getDescripcion());
        assertNull(producto.getPrecio());
        assertNull(producto.getStock());
        assertNull(producto.getImagenUrl());
        assertNull(producto.getCategoria());
        assertNull(producto.getActivo());
    }

    @Test
    void testValorPorDefectoActivo() {
        Producto productoPorDefecto = new Producto();
        assertTrue(productoPorDefecto.getActivo());
    }

    @Test
    void testPrecioCero() {
        producto.setPrecio(0.0);
        assertEquals(0.0, producto.getPrecio());
    }

    @Test
    void testStockCero() {
        producto.setStock(0);
        assertEquals(0, producto.getStock());
    }

    @Test
    void testActivoFalse() {
        producto.setActivo(false);
        assertFalse(producto.getActivo());
    }
}
