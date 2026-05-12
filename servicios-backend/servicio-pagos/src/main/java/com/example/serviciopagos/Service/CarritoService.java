package com.example.serviciopagos.Service;

import com.example.serviciopagos.Model.CarritoItem;
import com.example.serviciopagos.Repository.CarritoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    public List<CarritoItem> listarPorCliente(Long clienteId) {
        return carritoItemRepository.findByClienteId(clienteId);
    }

    public CarritoItem guardarItem(CarritoItem item) {
        return carritoItemRepository.save(item);
    }

    public void eliminarItem(Long id) {
        carritoItemRepository.deleteById(id);
    }

    public void limpiarCarrito(Long clienteId) {
        carritoItemRepository.deleteByClienteId(clienteId);
    }
}