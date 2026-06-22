package com.gamebakes.servicio_pedidos.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamebakes.servicio_pedidos.model.Pedido;
import com.gamebakes.servicio_pedidos.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class PedidoListener {

    @Autowired
    private PedidoRepository pedidoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "pago-exitoso-topic", groupId = "pedidos-group")
    public void escucharPago(String message) {
        try {
            System.out.println("Recibiendo notificación de pago para crear pedido...");
            System.out.println("Mensaje recibido: " + message);

            // Convertimos el String a un objeto JSON manipulable
            JsonNode jsonNode = objectMapper.readTree(message);

            if (!jsonNode.has("clienteId")) {
                System.err.println("No se pudo extraer clienteId del mensaje");
                return;
            }

            // Extraemos los datos exactos que manda Pagos
            Long clienteId = jsonNode.get("clienteId").asLong();
            String clienteNombre = jsonNode.has("clienteNombre") ? jsonNode.get("clienteNombre").asText() : "Cliente";

            // Verificamos si vienen los datos del producto en el JSON
            if (jsonNode.has("productoId") && jsonNode.has("cantidad")) {
                Long productoId = jsonNode.get("productoId").asLong();
                Integer cantidad = jsonNode.get("cantidad").asInt();

                System.out.println("Encontrado productoId: " + productoId + ", cantidad: " + cantidad);
                crearPedido(clienteId, clienteNombre, productoId, cantidad);
            } else {
                // Fallback de emergencia por si faltan datos del producto
                Pedido nuevoPedido = new Pedido();
                nuevoPedido.setClienteId(clienteId);
                nuevoPedido.setClienteNombre(clienteNombre);
                nuevoPedido.setVendedorId(1L);
                nuevoPedido.setProductoNombre("Producto Comprado");
                nuevoPedido.setEstado("PENDIENTE");
                nuevoPedido.setCantidad(1);

                pedidoRepository.save(nuevoPedido);
                System.out.println("¡Pedido creado de emergencia para el cliente: " + nuevoPedido.getClienteId());
            }

        } catch (Exception e) {
            System.err.println("Error al procesar mensaje de Kafka en formato JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void crearPedido(Long clienteId, String clienteNombre, Long productoId, Integer cantidad) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String productoUrl = "http://servicio-productos:8085/api/productos/" + productoId;
            Map<String, Object> productoData = restTemplate.getForObject(productoUrl, Map.class);

            if (productoData != null) {
                Pedido nuevoPedido = new Pedido();
                nuevoPedido.setClienteId(clienteId);
                nuevoPedido.setClienteNombre(clienteNombre != null ? clienteNombre : "Cliente");
                nuevoPedido.setProductoId(productoId);
                nuevoPedido.setProductoNombre(productoData.get("nombre") != null ? productoData.get("nombre").toString() : "Producto");
                nuevoPedido.setVendedorId(productoData.get("vendedorId") != null ? Long.valueOf(productoData.get("vendedorId").toString()) : 1L);
                nuevoPedido.setCantidad(cantidad);
                nuevoPedido.setEstado("PENDIENTE");

                pedidoRepository.save(nuevoPedido);
                System.out.println("¡Pedido creado exitosamente para el cliente: " + nuevoPedido.getClienteId() + ", producto: " + nuevoPedido.getProductoNombre());
            }
        } catch (Exception e) {
            System.err.println("Error al crear pedido consultando el servicio de productos " + productoId + ": " + e.getMessage());
        }
    }
}