package com.example.serviciopagos.Controller;

import com.example.serviciopagos.Model.CarritoItem;
import com.example.serviciopagos.Service.CarritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CarritoControllerTest {

    @Mock
    private CarritoService carritoService;

    @InjectMocks
    private CarritoController carritoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtener_Exito() {
        when(carritoService.listarPorCliente(1L)).thenReturn(List.of(new CarritoItem()));

        ResponseEntity<List<CarritoItem>> response = carritoController.obtener(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void agregar_Exito() {
        CarritoItem item = new CarritoItem();
        when(carritoService.guardarItem(item)).thenReturn(item);

        ResponseEntity<?> response = carritoController.agregar(item);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void agregar_FallaValidacion_RetornaBadRequest() {
        CarritoItem item = new CarritoItem();
        when(carritoService.guardarItem(item)).thenThrow(new RuntimeException("Producto agotado"));

        ResponseEntity<?> response = carritoController.agregar(item);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Producto agotado", response.getBody());
    }

    @Test
    void eliminar_Exito() {
        ResponseEntity<Void> response = carritoController.eliminar(1L);

        verify(carritoService, times(1)).eliminarItem(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}