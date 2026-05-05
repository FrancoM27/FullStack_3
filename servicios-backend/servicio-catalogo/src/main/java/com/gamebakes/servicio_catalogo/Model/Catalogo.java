package com.gamebakes.servicio_catalogo.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Catalogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private Double precioProducto;
    private Integer stockProducto;
    private String categoriaProducto; 
    private String imagenProducto;
    private Long idVendedor;

}

