package com.gamebakes.servicio_pedidos.kafka;

import com.gamebakes.servicio_pedidos.model.Pedido;
import com.gamebakes.servicio_pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PedidoListener {

    @Autowired
    private PedidoRepository pedidoRepository;

    @KafkaListener(topics = "pago-exitoso-topic", groupId = "pedidos-group")
    public void escucharPago(Map<String, Object> pagoInfo) {
        try {
            System.out.println("Recibiendo notificación de pago para crear pedido...");

            Pedido nuevoPedido = new Pedido();
            
            //Extraemos la info que viene del mapa de Kafka
            nuevoPedido.setClienteId(Long.valueOf(pagoInfo.get("clienteId").toString()));
            nuevoPedido.setProductoId(Long.valueOf(pagoInfo.get("productoId").toString()));
            
            //Por ahora asignamos un vendedor por defecto (ID 1) 
            //hasta que el front envíe el vendedorId real en el pago
            nuevoPedido.setVendedorId(1L); 
            
            //Datos estéticos (puedes expandirlos luego)
            nuevoPedido.setProductoNombre("Producto Comprado"); 
            nuevoPedido.setEstado("PENDIENTE");

            pedidoRepository.save(nuevoPedido);
            
            System.out.println("¡Pedido creado exitosamente para el cliente: " + nuevoPedido.getClienteId());

        } catch (Exception e) {
            System.err.println("Error al procesar mensaje de Kafka: " + e.getMessage());
        }
    }
    
}
