package com.example.serviciopagos.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    private Long pedidoId;
    private Long clienteId;
    private Double monto;
    private String metodoPago;
    private String estado;
    private String transaccionId;
    private LocalDateTime fechaPago;
    private Long productoId;
    private Integer cantidad;
}
