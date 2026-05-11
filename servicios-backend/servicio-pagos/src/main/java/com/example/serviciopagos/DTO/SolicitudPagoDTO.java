package com.example.serviciopagos.DTO;

import lombok.Data;

@Data
public class SolicitudPagoDTO {
    private Long pedidoId;
    private Long clienteId;
    private Double monto;
    private String metodo;
}
