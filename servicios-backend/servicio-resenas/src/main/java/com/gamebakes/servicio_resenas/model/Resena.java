package com.gamebakes.servicio_resenas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productoId;
    private String clienteNombre;
    private String comentario;
    private int estrellas;
    private String respuestaVendedor;
}