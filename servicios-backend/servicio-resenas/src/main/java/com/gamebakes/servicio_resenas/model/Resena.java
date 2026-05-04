package com.gamebakes.servicio_resenas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "resenas")
@Data
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;
    
    //Identificación del Cliente
    private Long clienteId;       
    private String clienteNombre; 

    @Column(length = 1000)
    private String comentario;
    
    private int estrellas; //Del 1 al 5

    // Interacción del Vendedor
    private String respuestaVendedor; 
    private Long vendedorId; // ID del vendedor dueño del producto
}