package com.example.serviciopagos.Controller;

import com.example.serviciopagos.DTO.SolicitudPagoDTO;
import com.example.serviciopagos.Model.Pago;
import com.example.serviciopagos.Service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PagoControllerTest {

    @Mock
    private PagoService pagoService;

    @InjectMocks
    private PagoController pagoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void iniciar_Exito() {
        SolicitudPagoDTO dto = new SolicitudPagoDTO();
        Pago pagoMock = new Pago();
        when(pagoService.iniciarPagoMP(any(SolicitudPagoDTO.class))).thenReturn(pagoMock);

        ResponseEntity<?> response = pagoController.iniciar(dto, "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(pagoService, times(1)).iniciarPagoMP(any(SolicitudPagoDTO.class));
    }

    @Test
    void iniciar_FalloService_LanzaBadRequest() {
        SolicitudPagoDTO dto = new SolicitudPagoDTO();
        when(pagoService.iniciarPagoMP(any(SolicitudPagoDTO.class))).thenThrow(new RuntimeException("Error MP"));

        ResponseEntity<?> response = pagoController.iniciar(dto, "1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error MP", response.getBody());
    }

    @Test
    void iniciar_FalloParseoId_LanzaBadRequest() {
        SolicitudPagoDTO dto = new SolicitudPagoDTO();
        // Mandamos "letras" para forzar el catch del NumberFormatException
        ResponseEntity<?> response = pagoController.iniciar(dto, "letras");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void confirmar_Exito() {
        Pago pagoMock = new Pago();
        when(pagoService.confirmarPago(1L, 1L, "token", "Luis")).thenReturn(pagoMock);

        ResponseEntity<Pago> response = pagoController.confirmar(1L, "1", "token", "Luis");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void confirmar_Exito_SinNombre() {
        Pago pagoMock = new Pago();
        when(pagoService.confirmarPago(1L, 1L, "token", "Cliente")).thenReturn(pagoMock);

        ResponseEntity<Pago> response = pagoController.confirmar(1L, "1", "token", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void historialPropio_Exito() {
        when(pagoService.obtenerHistorialPorCliente(1L)).thenReturn(List.of(new Pago()));

        ResponseEntity<List<Pago>> response = pagoController.historialPropio("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void iniciarDesdeCarrito_Exito() {
        Pago pagoMock = new Pago();
        when(pagoService.iniciarPagoCarrito(1L)).thenReturn(pagoMock);

        ResponseEntity<?> response = pagoController.iniciarDesdeCarrito(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void iniciarDesdeCarrito_FalloService_LanzaBadRequest() {
        when(pagoService.iniciarPagoCarrito(1L)).thenThrow(new RuntimeException("Carrito Vacio"));

        ResponseEntity<?> response = pagoController.iniciarDesdeCarrito(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Carrito Vacio", response.getBody());
    }
}