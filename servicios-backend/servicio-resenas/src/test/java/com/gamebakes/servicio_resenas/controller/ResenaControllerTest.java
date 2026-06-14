package com.gamebakes.servicio_resenas.controller;

import com.gamebakes.servicio_resenas.model.Resena;
import com.gamebakes.servicio_resenas.service.ResenaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaControllerTest {

    @Mock
    private ResenaService resenaService;

    @InjectMocks
    private ResenaController resenaController;

    private Resena resenaTest;

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

    @Test
    void testListarPorProducto() {
        // SETUP
        List<Resena> resenas = Arrays.asList(resenaTest);
        when(resenaService.obtenerPorProducto(1L)).thenReturn(resenas);

        // EXECUTION
        ResponseEntity<List<Resena>> resultado = resenaController.listarPorProducto(1L);

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(1, resultado.getBody().size());
        assertEquals("Excelente producto", resultado.getBody().get(0).getComentario());
        verify(resenaService, times(1)).obtenerPorProducto(1L);
    }

    @Test
    void testCrear_ConUserId() {
        // SETUP
        when(resenaService.guardarResena(any(Resena.class))).thenReturn(resenaTest);

        // EXECUTION
        ResponseEntity<Resena> resultado = resenaController.crear(resenaTest, "1");

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(1L, resultado.getBody().getClienteId());
        verify(resenaService, times(1)).guardarResena(any(Resena.class));
    }

    @Test
    void testCrear_SinUserId() {
        // SETUP
        when(resenaService.guardarResena(any(Resena.class))).thenReturn(resenaTest);

        // EXECUTION
        ResponseEntity<Resena> resultado = resenaController.crear(resenaTest, null);

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCodeValue());
        verify(resenaService, times(1)).guardarResena(any(Resena.class));
    }

    @Test
    void testListarPorVendedor() {
        // SETUP
        List<Resena> resenas = Arrays.asList(resenaTest);
        when(resenaService.obtenerPorVendedor(1L)).thenReturn(resenas);

        // EXECUTION
        ResponseEntity<List<Resena>> resultado = resenaController.listarPorVendedor(1L);

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(1, resultado.getBody().size());
        assertEquals(1L, resultado.getBody().get(0).getVendedorId());
        verify(resenaService, times(1)).obtenerPorVendedor(1L);
    }

    @Test
    void testListarPorCliente_ConUserId() {
        // SETUP
        List<Resena> resenas = Arrays.asList(resenaTest);
        when(resenaService.obtenerPorCliente(1L)).thenReturn(resenas);

        // EXECUTION
        ResponseEntity<List<Resena>> resultado = resenaController.listarPorCliente("1");

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals(1, resultado.getBody().size());
        assertEquals(1L, resultado.getBody().get(0).getClienteId());
        verify(resenaService, times(1)).obtenerPorCliente(1L);
    }

    @Test
    void testListarPorCliente_SinUserId() {
        // EXECUTION
        ResponseEntity<List<Resena>> resultado = resenaController.listarPorCliente(null);

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(400, resultado.getStatusCodeValue());
        verify(resenaService, never()).obtenerPorCliente(anyLong());
    }

    @Test
    void testResponder() {
        // SETUP
        resenaTest.setRespuestaVendedor("Gracias por tu reseña");
        when(resenaService.responderResena(1L, "Gracias por tu reseña")).thenReturn(resenaTest);

        // EXECUTION
        ResponseEntity<Resena> resultado = resenaController.responder(1L, "Gracias por tu reseña");

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals("Gracias por tu reseña", resultado.getBody().getRespuestaVendedor());
        verify(resenaService, times(1)).responderResena(1L, "Gracias por tu reseña");
    }

    @Test
    void testResponder_ConComillas() {
        // SETUP
        resenaTest.setRespuestaVendedor("Gracias por tu reseña");
        when(resenaService.responderResena(1L, "Gracias por tu reseña")).thenReturn(resenaTest);

        // EXECUTION
        ResponseEntity<Resena> resultado = resenaController.responder(1L, "\"Gracias por tu reseña\"");

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(200, resultado.getStatusCodeValue());
        assertEquals("Gracias por tu reseña", resultado.getBody().getRespuestaVendedor());
        verify(resenaService, times(1)).responderResena(1L, "Gracias por tu reseña");
    }
}
