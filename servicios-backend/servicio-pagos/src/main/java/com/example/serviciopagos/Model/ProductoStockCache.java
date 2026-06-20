package com.example.serviciopagos.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "producto_stock_cache")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoStockCache {

    @Id
    private Long productoId;
    private Integer stockDisponible;
}
