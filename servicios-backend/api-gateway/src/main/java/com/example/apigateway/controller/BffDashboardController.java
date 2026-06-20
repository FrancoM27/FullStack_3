package com.example.apigateway.controller;

import com.example.apigateway.client.PedidoClient;
import com.example.apigateway.client.PerfilClient;
import com.example.apigateway.client.ResenaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bff/dashboard")
public class BffDashboardController {

    @Autowired
    private PedidoClient pedidoClient;

    @Autowired
    private PerfilClient perfilClient;

    @Autowired
    private ResenaClient resenaClient;

    @GetMapping("/cliente/{clienteId}")
    public Map<String, Object> obtenerDashboardCliente(@PathVariable Long clienteId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Obtener perfil del cliente
            Map<String, Object> perfil = perfilClient.obtenerPerfil(clienteId);
            response.put("perfil", perfil);
            
            // Obtener pedidos del cliente
            Map<String, Object>[] pedidos = pedidoClient.obtenerMisPedidos();
            response.put("pedidos", pedidos);
            
            // Obtener reseñas del cliente
            Map<String, Object>[] resenas = resenaClient.obtenerResenasCliente();
            response.put("resenas", resenas);
            
            // Calcular estadísticas
            response.put("estadisticas", calcularEstadisticas(pedidos, resenas));
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        
        return response;
    }
    
    @GetMapping("/vendedor/{vendedorId}")
    public Map<String, Object> obtenerDashboardVendedor(@PathVariable Long vendedorId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Obtener perfil del vendedor
            Map<String, Object> perfil = perfilClient.obtenerPerfil(vendedorId);
            response.put("perfil", perfil);
            
            // Obtener pedidos del vendedor
            Map<String, Object>[] pedidos = pedidoClient.obtenerPedidosVendedor(vendedorId);
            response.put("pedidos", pedidos);
            
            // Obtener reseñas del vendedor
            Map<String, Object>[] resenas = resenaClient.obtenerResenasVendedor(vendedorId);
            response.put("resenas", resenas);
            
            // Calcular estadísticas
            response.put("estadisticas", calcularEstadisticas(pedidos, resenas));
            
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        
        return response;
    }
    
    private Map<String, Object> calcularEstadisticas(Map<String, Object>[] pedidos, Map<String, Object>[] resenas) {
        Map<String, Object> estadisticas = new HashMap<>();
        
        int totalPedidos = pedidos != null ? pedidos.length : 0;
        int totalResenas = resenas != null ? resenas.length : 0;
        
        int pedidosEntregados = 0;
        int pedidosEnCamino = 0;
        int pedidosPreparacion = 0;
        
        if (pedidos != null) {
            for (Map<String, Object> pedido : pedidos) {
                String estado = (String) pedido.get("estado");
                if ("ENTREGADO".equals(estado)) pedidosEntregados++;
                else if ("EN_CAMINO".equals(estado)) pedidosEnCamino++;
                else if ("PREPARACION".equals(estado)) pedidosPreparacion++;
            }
        }
        
        double promedioEstrellas = 0.0;
        if (resenas != null && resenas.length > 0) {
            double sumaEstrellas = 0.0;
            for (Map<String, Object> resena : resenas) {
                Number estrellas = (Number) resena.get("estrellas");
                if (estrellas != null) {
                    sumaEstrellas += estrellas.doubleValue();
                }
            }
            promedioEstrellas = sumaEstrellas / resenas.length;
        }
        
        estadisticas.put("totalPedidos", totalPedidos);
        estadisticas.put("totalResenas", totalResenas);
        estadisticas.put("pedidosEntregados", pedidosEntregados);
        estadisticas.put("pedidosEnCamino", pedidosEnCamino);
        estadisticas.put("pedidosPreparacion", pedidosPreparacion);
        estadisticas.put("promedioEstrellas", promedioEstrellas);
        
        return estadisticas;
    }
}
