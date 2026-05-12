package com.gamebakes.servicio_pedidos.controller;

import com.gamebakes.servicio_pedidos.model.Pedido;
import com.gamebakes.servicio_pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    //CONSULTA DE CLIENTE: Ahora es más seguro porque usamos el ID del Header
    @GetMapping("/mis-pedidos")
    public List<Pedido> listarMisPedidos(@RequestHeader(value = "X-User-Id", required = false) String userId) {
        if (userId == null) return List.of(); //O lanzar una excepción de seguridad
        return pedidoService.obtenerPedidosPorCliente(Long.parseLong(userId));
    }

    //El vendedor sigue consultando por su ID específico
    @GetMapping("/vendedor/{vendedorId}")
    public List<Pedido> listarPorVendedor(@PathVariable Long vendedorId) {
        return pedidoService.obtenerPedidosPorVendedor(vendedorId);
    }

    //CREACIÓN: Se asigna el clienteId desde el token validado por el Gateway
    @PostMapping
    public Pedido crear(
            @RequestBody Pedido pedido,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        if (userId != null) {
            pedido.setClienteId(Long.parseLong(userId));
        }
        return pedidoService.crearPedido(pedido);
    }

    @PutMapping("/{id}/estado")
    public Pedido actualizar(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return pedidoService.actualizarEstado(id, nuevoEstado);
    }
}