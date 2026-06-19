package com.gamebakes.servicio_pedidos.kafka;

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

    @KafkaListener(topics = "pago-exitoso-topic", groupId = "pedidos-group")
    public void escucharPago(String message) {
        try {
            System.out.println("Recibiendo notificación de pago para crear pedido...");
            System.out.println("Mensaje recibido: " + message);

            // Parsear manualmente el String para extraer información
            Long clienteId = null;
            String clienteNombre = null;
            String itemsStr = null;

            // Extraer clienteId
            if (message.contains("clienteId=")) {
                String[] parts = message.split("clienteId=");
                if (parts.length > 1) {
                    String clientIdPart = parts[1].split(",")[0].trim();
                    clienteId = Long.valueOf(clientIdPart);
                }
            }

            // Extraer clienteNombre
            if (message.contains("clienteNombre=")) {
                String[] parts = message.split("clienteNombre=");
                if (parts.length > 1) {
                    String clientNamePart = parts[1].split(",")[0].trim();
                    clienteNombre = clientNamePart;
                }
            }

            // Extraer items (desde items= hasta el cierre del corchete)
            if (message.contains("items=")) {
                int itemsStart = message.indexOf("items=") + 6;
                int itemsEnd = message.indexOf("]", itemsStart);
                if (itemsEnd > itemsStart) {
                    itemsStr = message.substring(itemsStart, itemsEnd + 1);
                }
            }

            if (clienteId == null) {
                System.err.println("No se pudo extraer clienteId del mensaje");
                return;
            }

            // Si hay items en el carrito, procesarlos
            if (itemsStr != null && !itemsStr.isEmpty() && !itemsStr.equals("[]")) {
                System.out.println("Procesando items del carrito: " + itemsStr);
                // Usar regex para extraer productoId y cantidad de cada CarritoItem
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("productoId=(\\d+).*?cantidad=(\\d+)");
                java.util.regex.Matcher matcher = pattern.matcher(itemsStr);

                while (matcher.find()) {
                    try {
                        Long productoId = Long.valueOf(matcher.group(1));
                        Integer cantidad = Integer.valueOf(matcher.group(2));
                        System.out.println("Encontrado productoId: " + productoId + ", cantidad: " + cantidad);
                        crearPedido(clienteId, clienteNombre, productoId, cantidad);
                    } catch (Exception e) {
                        System.err.println("Error al procesar item: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else {
                // Si no hay items (pago directo), crear un pedido genérico
                Pedido nuevoPedido = new Pedido();
                nuevoPedido.setClienteId(clienteId);
                nuevoPedido.setClienteNombre(clienteNombre != null ? clienteNombre : "Cliente");
                nuevoPedido.setVendedorId(1L);
                nuevoPedido.setProductoNombre("Producto Comprado");
                nuevoPedido.setEstado("PENDIENTE");
                nuevoPedido.setCantidad(1);

                pedidoRepository.save(nuevoPedido);
                System.out.println("¡Pedido creado exitosamente para el cliente: " + nuevoPedido.getClienteId());
            }

        } catch (Exception e) {
            System.err.println("Error al procesar mensaje de Kafka: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void crearPedido(Long clienteId, String clienteNombre, Long productoId, Integer cantidad) {
        try {
            // Obtener información del producto desde el servicio de productos
            RestTemplate restTemplate = new RestTemplate();
            String productoUrl = "http://18.205.233.123:8085/api/productos/" + productoId;
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
            System.err.println("Error al crear pedido para producto " + productoId + ": " + e.getMessage());
        }
    }

}
