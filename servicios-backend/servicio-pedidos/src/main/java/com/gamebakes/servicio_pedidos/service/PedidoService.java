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

    //VISTA CLIENTE: Obtener sus pedidos personales
    public List<Pedido> obtenerPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    //VISTA VENDEDOR: Obtener pedidos dirigidos a sus productos
    public List<Pedido> obtenerPedidosPorVendedor(Long vendedorId) {
        return pedidoRepository.findByVendedorId(vendedorId);
    }

    public Pedido crearPedido(Pedido pedido) {
        pedido.setEstado("PENDIENTE");
        Pedido nuevoPedido = pedidoRepository.save(pedido);
        
        //Kafka: Notificar creación
        kafkaTemplate.send(TOPIC, "NUEVO_PEDIDO: El cliente " + pedido.getClienteNombre() + " compró " + pedido.getProductoNombre());
        
        return nuevoPedido;
    }

    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.setEstado(nuevoEstado);
        Pedido actualizado = pedidoRepository.save(pedido);
        
        //Kafka: Notificar cambio de estado para seguimiento en tiempo real
        kafkaTemplate.send(TOPIC, "ESTADO_ACTUALIZADO: Pedido #" + id + " ahora está en " + nuevoEstado);
        
        return actualizado;
    }
}