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
        String message = "clienteId=1, clienteNombre=Juan, items=[productoId=100, cantidad=2]";
        
        // Este test solo verifica que el método se ejecuta sin lanzar excepciones
        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithValidMessageWithoutItems_ProcessesCorrectly() {
        String message = "clienteId=1, clienteNombre=Juan, items=[]";
        
        pedidoListener.escucharPago(message);
        
        verify(pedidoRepository, atLeastOnce()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithMessageWithoutItems_ProcessesCorrectly() {
        String message = "clienteId=1, clienteNombre=Juan";
        
        pedidoListener.escucharPago(message);
        
        verify(pedidoRepository, atLeastOnce()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithNullClienteId_DoesNotProcess() {
        String message = "clienteNombre=Juan, items=[productoId=100, cantidad=2]";
        
        pedidoListener.escucharPago(message);
        
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithEmptyMessage_DoesNotProcess() {
        String message = "";
        
        pedidoListener.escucharPago(message);
        
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithInvalidMessage_DoesNotProcess() {
        String message = "invalid message";
        
        pedidoListener.escucharPago(message);
        
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testEscucharPago_WithMultipleItems_ProcessesAllItems() {
        String message = "clienteId=1, clienteNombre=Juan, items=[productoId=100, cantidad=2, productoId=200, cantidad=1]";
        
        // Este test solo verifica que el método se ejecuta sin lanzar excepciones
        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithMessageWithoutClienteNombre_ProcessesCorrectly() {
        String message = "clienteId=1, items=[productoId=100, cantidad=2]";
        
        // Este test solo verifica que el método se ejecuta sin lanzar excepciones
        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithMalformedItems_ProcessesCorrectly() {
        String message = "clienteId=1, clienteNombre=Juan, items=[malformed data]";
        
        // Este test verifica que el método maneja items mal formados sin lanzar excepciones
        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithNullMessage_DoesNotThrow() {
        String message = null;
        
        // Este test verifica que el método maneja mensajes nulos sin lanzar excepciones
        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithItemsButNoProductoId_ProcessesCorrectly() {
        String message = "clienteId=1, clienteNombre=Juan, items=[cantidad=2]";
        
        // Este test verifica que el método maneja items sin productoId
        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }

    @Test
    void testEscucharPago_WithItemsButNoCantidad_ProcessesCorrectly() {
        String message = "clienteId=1, clienteNombre=Juan, items=[productoId=100]";
        
        // Este test verifica que el método maneja items sin cantidad
        assertDoesNotThrow(() -> pedidoListener.escucharPago(message));
    }
}
