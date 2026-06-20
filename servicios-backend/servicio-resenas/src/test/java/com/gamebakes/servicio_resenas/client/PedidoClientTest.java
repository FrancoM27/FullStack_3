package com.gamebakes.servicio_resenas.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoClientTest {

    @Test
    void testFallbackValidarCompra_ReturnsFalse() {
        PedidoClient client = new PedidoClient() {
            @Override
            public boolean validarCompra(Long clienteId, Long productoId) {
                return true;
            }

            @Override
            public boolean validarEntregado(Long clienteId, Long productoId) {
                return true;
            }
        };

        boolean result = client.fallbackValidarCompra(1L, 1L, new RuntimeException("Test error"));
        assertFalse(result);
    }

    @Test
    void testFallbackValidarEntregado_ReturnsFalse() {
        PedidoClient client = new PedidoClient() {
            @Override
            public boolean validarCompra(Long clienteId, Long productoId) {
                return true;
            }

            @Override
            public boolean validarEntregado(Long clienteId, Long productoId) {
                return true;
            }
        };

        boolean result = client.fallbackValidarEntregado(1L, 1L, new RuntimeException("Test error"));
        assertFalse(result);
    }

    @Test
    void testFallbackValidarCompra_WithDifferentParameters_ReturnsFalse() {
        PedidoClient client = new PedidoClient() {
            @Override
            public boolean validarCompra(Long clienteId, Long productoId) {
                return true;
            }

            @Override
            public boolean validarEntregado(Long clienteId, Long productoId) {
                return true;
            }
        };

        boolean result = client.fallbackValidarCompra(999L, 888L, new RuntimeException("Test error"));
        assertFalse(result);
    }

    @Test
    void testFallbackValidarEntregado_WithDifferentParameters_ReturnsFalse() {
        PedidoClient client = new PedidoClient() {
            @Override
            public boolean validarCompra(Long clienteId, Long productoId) {
                return true;
            }

            @Override
            public boolean validarEntregado(Long clienteId, Long productoId) {
                return true;
            }
        };

        boolean result = client.fallbackValidarEntregado(999L, 888L, new RuntimeException("Test error"));
        assertFalse(result);
    }
}
