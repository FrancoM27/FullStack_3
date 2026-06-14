package com.gamebakes.servicio_resenas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resenas")
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;
    private String productoNombre;

    //Identificación del Cliente
    private Long clienteId;
    private String clienteNombre;

    @Column(length = 1000)
    private String comentario;

    private int estrellas; //Del 1 al 5

    //Interacción del Vendedor
    private String respuestaVendedor;
    private Long vendedorId; //ID del vendedor dueño del producto

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas;
    }

    public String getRespuestaVendedor() {
        return respuestaVendedor;
    }

    public void setRespuestaVendedor(String respuestaVendedor) {
        this.respuestaVendedor = respuestaVendedor;
    }

    public Long getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(Long vendedorId) {
        this.vendedorId = vendedorId;
    }
}