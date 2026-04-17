package com.gamebakes.servicio_pedidos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String clienteNombre;
    private String productoNombre;
    private String estado; // PENDIENTE, EN_PREPARACION, ENVIADO, ENTREGADO
}