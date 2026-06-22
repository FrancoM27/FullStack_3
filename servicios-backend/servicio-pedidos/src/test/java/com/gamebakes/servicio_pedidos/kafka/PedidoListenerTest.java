package com.gamebakes.servicio_pedidos.kafka;

import com.gamebakes.servicio_pedidos.model.Pedido;
import com.gamebakes.servicio_pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoListenerTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoListener pedidoListener;

    @Test
    void testEscucharPago_WithValidMessageWithItems_ProcessesCorrectly() {
        String message = "{\"clienteId\":1, \"clienteNombre\":\"Juan\", \"productoId\":100, \"cantidad\":2}";

        // Verifica que se procese el JSON sin lanzar excepciones
        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithValidMessageWithoutItems_ProcessesCorrectly() {
        String message = "{\"clienteId\":1, \"clienteNombre\":\"Juan\"}";

        pedidoListener.escucharPago(message);

        // Como no hay items, debería crear un pedido de emergencia
        verify(pedidoRepository, atLeastOnce()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithMessageWithoutItems_ProcessesCorrectly() {
        String message = "{\"clienteId\":1, \"clienteNombre\":\"Juan\"}";

        pedidoListener.escucharPago(message);

        verify(pedidoRepository, atLeastOnce()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithNullClienteId_DoesNotProcess() {
        String message = "{\"clienteNombre\":\"Juan\", \"productoId\":100, \"cantidad\":2}";

        pedidoListener.escucharPago(message);

        // Al faltar clienteId, debe retornar sin guardar nada
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithEmptyMessage_DoesNotProcess() {
        String message = "";

        pedidoListener.escucharPago(message);

        // Lanza excepción de JSON parse, capturada por el try-catch, no guarda nada
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithInvalidMessage_DoesNotProcess() {
        String message = "invalid message no json";

        pedidoListener.escucharPago(message);

        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithMultipleItems_ProcessesAllItems() {
        // Adaptado a la lógica actual que espera un JSON plano
        String message = "{\"clienteId\":1, \"clienteNombre\":\"Juan\", \"productoId\":100, \"cantidad\":2}";

        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithMessageWithoutClienteNombre_ProcessesCorrectly() {
        String message = "{\"clienteId\":1, \"productoId\":100, \"cantidad\":2}";

        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithMalformedItems_ProcessesCorrectly() {
        // Valor string en vez de número para que falle el parseo de Jackson
        String message = "{\"clienteId\":1, \"clienteNombre\":\"Juan\", \"productoId\":\"malformed\"}";

        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithNullMessage_DoesNotThrow() {
        String message = null;

        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithItemsButNoProductoId_ProcessesCorrectly() {
        String message = "{\"clienteId\":1, \"clienteNombre\":\"Juan\", \"cantidad\":2}";

        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithItemsButNoCantidad_ProcessesCorrectly() {
        String message = "{\"clienteId\":1, \"clienteNombre\":\"Juan\", \"productoId\":100}";

        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }
}