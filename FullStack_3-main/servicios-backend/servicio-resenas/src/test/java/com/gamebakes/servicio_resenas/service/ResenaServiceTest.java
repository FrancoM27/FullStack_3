package com.gamebakes.servicio_resenas.service;

import com.gamebakes.servicio_resenas.client.PedidoClient;
import com.gamebakes.servicio_resenas.model.Resena;
import com.gamebakes.servicio_resenas.repository.ResenaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private ResenaService resenaService;

    private Resena resenaTest;

    // SETUP: Preparación del entorno y datos de prueba
    @BeforeEach
    void setUp() {
        resenaTest = new Resena();
        resenaTest.setId(1L);
        resenaTest.setClienteId(1L);
        resenaTest.setProductoId(1L);
        resenaTest.setVendedorId(1L);
        resenaTest.setComentario("Excelente producto");
        resenaTest.setEstrellas(5);
    }

    // EXECUTION + VERIFICATION: Ejecución y comparación del resultado
    @Test
    void testObtenerPorProducto() {
        // SETUP
        List<Resena> resenasEsperadas = Arrays.asList(resenaTest);
        when(resenaRepository.findByProductoId(1L)).thenReturn(resenasEsperadas);

        // EXECUTION
        List<Resena> resultado = resenaService.obtenerPorProducto(1L);

        // VERIFICATION
        assertEquals(1, resultado.size());
        assertEquals("Excelente producto", resultado.get(0).getComentario());
        verify(resenaRepository, times(1)).findByProductoId(1L);
    }

    @Test
    void testObtenerPorVendedor() {
        // SETUP
        List<Resena> resenasEsperadas = Arrays.asList(resenaTest);
        when(resenaRepository.findByVendedorId(1L)).thenReturn(resenasEsperadas);

        // EXECUTION
        List<Resena> resultado = resenaService.obtenerPorVendedor(1L);

        // VERIFICATION
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getVendedorId());
        verify(resenaRepository, times(1)).findByVendedorId(1L);
    }

    @Test
    void testObtenerPorCliente() {
        // SETUP
        List<Resena> resenasEsperadas = Arrays.asList(resenaTest);
        when(resenaRepository.findByClienteId(1L)).thenReturn(resenasEsperadas);

        // EXECUTION
        List<Resena> resultado = resenaService.obtenerPorCliente(1L);

        // VERIFICATION
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getClienteId());
        verify(resenaRepository, times(1)).findByClienteId(1L);
    }

    @Test
    void testGuardarResena_ConCompraValida() {
        // SETUP
        when(pedidoClient.validarCompra(1L, 1L)).thenReturn(true);
        when(pedidoClient.validarEntregado(1L, 1L)).thenReturn(true);
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaTest);

        // EXECUTION
        Resena resultado = resenaService.guardarResena(resenaTest);

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pedidoClient, times(1)).validarCompra(1L, 1L);
        verify(pedidoClient, times(1)).validarEntregado(1L, 1L);
        verify(resenaRepository, times(1)).save(resenaTest);
    }

    @Test
    void testGuardarResena_SinCompra() {
        // SETUP
        when(pedidoClient.validarCompra(1L, 1L)).thenReturn(false);

        // EXECUTION + VERIFICATION
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resenaService.guardarResena(resenaTest);
        });

        assertEquals("El cliente no ha comprado este producto. No puede dejar una reseña.", exception.getMessage());
        verify(pedidoClient, times(1)).validarCompra(1L, 1L);
        verify(pedidoClient, never()).validarEntregado(anyLong(), anyLong());
        verify(resenaRepository, never()).save(any(Resena.class));
    }

    @Test
    void testGuardarResena_SinEntrega() {
        // SETUP
        when(pedidoClient.validarCompra(1L, 1L)).thenReturn(true);
        when(pedidoClient.validarEntregado(1L, 1L)).thenReturn(false);

        // EXECUTION + VERIFICATION
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resenaService.guardarResena(resenaTest);
        });

        assertEquals("El pedido aún no ha sido entregado. Solo puede reseñar productos entregados.", exception.getMessage());
        verify(pedidoClient, times(1)).validarCompra(1L, 1L);
        verify(pedidoClient, times(1)).validarEntregado(1L, 1L);
        verify(resenaRepository, never()).save(any(Resena.class));
    }

    @Test
    void testResponderResena() {
        // SETUP
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resenaTest));
        when(resenaRepository.save(any(Resena.class))).thenReturn(resenaTest);

        // EXECUTION
        Resena resultado = resenaService.responderResena(1L, "Gracias por tu reseña");

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals("Gracias por tu reseña", resultado.getRespuestaVendedor());
        verify(resenaRepository, times(1)).findById(1L);
        verify(resenaRepository, times(1)).save(resenaTest);
    }

    @Test
    void testResponderResena_NoEncontrada() {
        // SETUP
        when(resenaRepository.findById(1L)).thenReturn(Optional.empty());

        // EXECUTION + VERIFICATION
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resenaService.responderResena(1L, "Gracias por tu reseña");
        });

        assertEquals("Reseña no encontrada", exception.getMessage());
        verify(resenaRepository, times(1)).findById(1L);
        verify(resenaRepository, never()).save(any(Resena.class));
    }

    @Test
    void testFallbackResenas() {
        // EXECUTION
        List<Resena> resultado = resenaService.fallbackResenas(1L, new RuntimeException("Error de conexión"));

        // VERIFICATION
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
    }

    @Test
    void testFallbackResenasVendedor() {
        // EXECUTION
        List<Resena> resultado = resenaService.fallbackResenasVendedor(1L, new RuntimeException("Error de conexión"));

        // VERIFICATION
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
    }

    @Test
    void testFallbackResenasCliente() {
        // EXECUTION
        List<Resena> resultado = resenaService.fallbackResenasCliente(1L, new RuntimeException("Error de conexión"));

        // VERIFICATION
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
    }

    // TEARDOWN: Limpieza (automático con JUnit 5)
}
