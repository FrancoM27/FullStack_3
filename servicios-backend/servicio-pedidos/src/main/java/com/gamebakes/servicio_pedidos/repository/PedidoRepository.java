package com.gamebakes.servicio_pedidos.repository;

import com.gamebakes.servicio_pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    //Vista Cliente: Ver el seguimiento de sus propias compras
    List<Pedido> findByClienteId(Long clienteId);
    
    //Vista Vendedor: Ver los pedidos que le han hecho a él
    List<Pedido> findByVendedorId(Long vendedorId);

}