package com.gamebakes.servicio_pedidos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clienteId; //ID del usuario que compra
    private String clienteNombre; 
    private Long productoId; //ID del producto
    private String productoNombre;
    private Long vendedorId; //ID del vendedor que debe preparar el pedido
    private String estado; //PENDIENTE, PREPARACION, EN_CAMINO, ENTREGADO
}