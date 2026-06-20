package com.example.serviciopagos.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "carrito_items")
@Data
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;
    private Long productoId;
    private Integer cantidad;
    private Double precioUnitario;
}