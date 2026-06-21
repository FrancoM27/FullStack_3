package com.gamebakes.serviciousuarios.Config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Test
    void globalExceptionHandler_CreacionExitosa() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        assertNotNull(handler);
    }

    @Test
    void handleRuntimeException_Exito() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        RuntimeException exception = new RuntimeException("Error de prueba");

        ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error de prueba", response.getBody().get("message"));
    }

    @Test
    void handleRuntimeException_ConMensajeNulo() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        RuntimeException exception = new RuntimeException();

        ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().get("message"));
    }
}
