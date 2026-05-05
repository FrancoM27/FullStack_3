package com.gamebakes.servicio_pedidos.controller;

import com.gamebakes.servicio_pedidos.model.Pedido;
import com.gamebakes.servicio_pedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    //El cliente consulta su seguimiento
    @GetMapping("/cliente/{clienteId}")
    public List<Pedido> listarPorCliente(@PathVariable Long clienteId) {
        return pedidoService.obtenerPedidosPorCliente(clienteId);
    }

    //El vendedor consulta los pedidos que debe gestionar
    @GetMapping("/vendedor/{vendedorId}")
    public List<Pedido> listarPorVendedor(@PathVariable Long vendedorId) {
        return pedidoService.obtenerPedidosPorVendedor(vendedorId);
    }

    @PostMapping
    public Pedido crear(@RequestBody Pedido pedido) {
        return pedidoService.crearPedido(pedido);
    }

    //El vendedor modifica el estado (PREPARACION, EN_CAMINO, etc.)
    @PutMapping("/{id}/estado")
    public Pedido actualizar(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return pedidoService.actualizarEstado(id, nuevoEstado);
    }
}