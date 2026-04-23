package com.gamebakes.servicio_pedidos.service;

import com.gamebakes.servicio_pedidos.model.Pedido;
import com.gamebakes.servicio_pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String TOPIC = "seguimiento-pedidos";

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido crearPedido(Pedido pedido) {
        pedido.setEstado("PENDIENTE");
        Pedido nuevoPedido = pedidoRepository.save(pedido);
        
        // Notificamos a Kafka que hay un nuevo pedido
        //kafkaTemplate.send(TOPIC, "Pedido #" + nuevoPedido.getId() + " creado para " + nuevoPedido.getClienteNombre()); (DESCOMENTAR LUEGO)
        
        return nuevoPedido;
    }

    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow();
        pedido.setEstado(nuevoEstado);
        
        // Notificamos a Kafka el cambio de estado
        //kafkaTemplate.send(TOPIC, "El pedido #" + id + " cambió a: " + nuevoEstado); (DESCOMENTAR LUEGO)
        
        return pedidoRepository.save(pedido);
    }
}