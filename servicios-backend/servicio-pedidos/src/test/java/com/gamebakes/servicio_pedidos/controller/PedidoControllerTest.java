package com.gamebakes.servicio_pedidos.controller;

import com.gamebakes.servicio_pedidos.model.Pedido;
import com.gamebakes.servicio_pedidos.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    private Pedido pedidoTest;

    @BeforeEach
    void setUp() {
        pedidoTest = new Pedido();
        pedidoTest.setId(1L);
        pedidoTest.setClienteId(1L);
        pedidoTest.setClienteNombre("Cliente 1");
        pedidoTest.setProductoId(1L);
        pedidoTest.setProductoNombre("Producto 1");
        pedidoTest.setVendedorId(1L);
        pedidoTest.setEstado("PENDIENTE");
        pedidoTest.setCantidad(2);
    }

    @Test
    void testListarMisPedidos_ConUserId() {
        // SETUP
        List<Pedido> pedidos = Arrays.asList(pedidoTest);
        when(pedidoService.obtenerPedidosPorCliente(1L)).thenReturn(pedidos);

        // EXECUTION
        List<Pedido> resultado = pedidoController.listarMisPedidos("1");

        // VERIFICATION
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getClienteId());
        verify(pedidoService, times(1)).obtenerPedidosPorCliente(1L);
    }

    @Test
    void testListarMisPedidos_SinUserId() {
        // EXECUTION
        List<Pedido> resultado = pedidoController.listarMisPedidos(null);

        // VERIFICATION
        assertTrue(resultado.isEmpty());
        verify(pedidoService, never()).obtenerPedidosPorCliente(anyLong());
    }

    @Test
    void testListarPorVendedor() {
        // SETUP
        List<Pedido> pedidos = Arrays.asList(pedidoTest);
        when(pedidoService.obtenerPedidosPorVendedor(1L)).thenReturn(pedidos);

        // EXECUTION
        List<Pedido> resultado = pedidoController.listarPorVendedor(1L);

        // VERIFICATION
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getVendedorId());
        verify(pedidoService, times(1)).obtenerPedidosPorVendedor(1L);
    }

    @Test
    void testCrear_ConUserId() {
        // SETUP
        when(pedidoService.crearPedido(any(Pedido.class))).thenReturn(pedidoTest);

        // EXECUTION
        Pedido resultado = pedidoController.crear(pedidoTest, "1");

        // VERIFICATION
        assertNotNull(resultado);
        assertEquals(1L, resultado.getClienteId());
        verify(pedidoService, times(1)).crearPedido(any(Pedido.class));
    }

    @Test
    void testCrear_SinUserId() {
        // SETUP
        when(pedidoService.crearPedido(any(Pedido.class))).thenReturn(pedidoTest);

        // EXECUTION
        Pedido resultado = pedidoController.crear(pedidoTest, null);

        // VERIFICATION
        assertNotNull(resultado);
        verify(pedidoService, times(1)).crearPedido(any(Pedido.class));
    }

    @Test
    void testActualizar() {
        // SETUP
        when(pedidoService.actualizarEstado(1L, "EN_CAMINO")).thenReturn(pedidoTest);

        // EXECUTION
        Pedido resultado = pedidoController.actualizar(1L, "EN_CAMINO");

        // VERIFICATION
        assertNotNull(resultado);
        verify(pedidoService, times(1)).actualizarEstado(1L, "EN_CAMINO");
    }

    @Test
    void testValidarCompra() {
        // SETUP
        when(pedidoService.validarCompra(1L, 1L)).thenReturn(true);

        // EXECUTION
        boolean resultado = pedidoController.validarCompra(1L, 1L);

        // VERIFICATION
        assertTrue(resultado);
        verify(pedidoService, times(1)).validarCompra(1L, 1L);
    }

    @Test
    void testValidarEntregado() {
        // SETUP
        when(pedidoService.validarEntregado(1L, 1L)).thenReturn(true);

        // EXECUTION
        boolean resultado = pedidoController.validarEntregado(1L, 1L);

        // VERIFICATION
        assertTrue(resultado);
        verify(pedidoService, times(1)).validarEntregado(1L, 1L);
    }
}
