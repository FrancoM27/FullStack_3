package com.example.serviciopagos.Service;

import com.example.serviciopagos.DTO.SolicitudPagoDTO;
import com.example.serviciopagos.Model.Pago;
import com.example.serviciopagos.Model.CarritoItem;
import com.example.serviciopagos.Repository.PagoRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.exceptions.MPApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private CarritoService carritoService;

    private String accessToken = "APP_USR-6384651523153058-051023-18ce169c7c92f41fc1af6ae5d5ad9a39-3392426062";

    public Pago iniciarPagoMP(SolicitudPagoDTO solicitud) {
        Pago pago = new Pago();
        pago.setPedidoId(solicitud.getPedidoId());
        pago.setProductoId(solicitud.getProductoId());
        pago.setCantidad(solicitud.getCantidad());
        pago.setClienteId(solicitud.getClienteId());
        pago.setMonto(solicitud.getMonto());
        pago.setMetodoPago("MERCADO_PAGO");
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("PENDIENTE");
        pago = pagoRepository.save(pago);

        try {
            MercadoPagoConfig.setAccessToken(accessToken);
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title("Compra Directa Gamebakes")
                    .quantity(1)
                    .unitPrice(new BigDecimal(solicitud.getMonto()))
                    .build();
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:5173/pago-exito")
                    .build();
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .externalReference(pago.getIdPago().toString())
                    .build();
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);
            pago.setTransaccionId(preference.getInitPoint());
            return pagoRepository.save(pago);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Pago iniciarPagoCarrito(Long clienteId) {
        List<CarritoItem> itemsCarrito = carritoService.listarPorCliente(clienteId);
        if (itemsCarrito.isEmpty()) throw new RuntimeException("Carrito vacío");

        Double total = itemsCarrito.stream()
                .mapToDouble(i -> i.getPrecioUnitario() * i.getCantidad())
                .sum();

        Pago pago = new Pago();
        pago.setClienteId(clienteId);
        pago.setMonto(total);
        pago.setMetodoPago("MERCADO_PAGO");
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("PENDIENTE");
        pago = pagoRepository.save(pago);

        try {
            MercadoPagoConfig.setAccessToken(accessToken);
            List<PreferenceItemRequest> itemsPreference = new ArrayList<>();
            for (CarritoItem ci : itemsCarrito) {
                itemsPreference.add(PreferenceItemRequest.builder()
                        .title("Producto #" + ci.getProductoId())
                        .quantity(ci.getCantidad())
                        .unitPrice(new BigDecimal(ci.getPrecioUnitario()))
                        .build());
            }
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:5173/pago-exito")
                    .build();
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(itemsPreference)
                    .backUrls(backUrls)
                    .externalReference(pago.getIdPago().toString())
                    .build();
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);
            pago.setTransaccionId(preference.getInitPoint());
            return pagoRepository.save(pago);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Pago confirmarPago(Long idPago, Long usuarioAutenticadoId) {
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (!pago.getClienteId().equals(usuarioAutenticadoId)) {
            throw new RuntimeException("No autorizado");
        }

        pago.setEstado("APROBADO");
        pago.setTransaccionId("MP-CONFIRM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        pago = pagoRepository.save(pago);

        try {
            RestTemplate restTemplate = new RestTemplate();

            if (pago.getProductoId() != null) {
                String url = "http://localhost:8085/api/productos/" + pago.getProductoId() + "/restar-stock?cantidad=" + pago.getCantidad();
                restTemplate.put(url, null);
            } else {
                List<CarritoItem> items = carritoService.listarPorCliente(pago.getClienteId());
                for (CarritoItem item : items) {
                    String url = "http://localhost:8085/api/productos/" + item.getProductoId() + "/restar-stock?cantidad=" + item.getCantidad();
                    restTemplate.put(url, null);
                }
            }

            carritoService.limpiarCarrito(pago.getClienteId());
        } catch (Exception e) {
            System.err.println("Error en post-pago: " + e.getMessage());
        }

        return pago;
    }

    public List<Pago> obtenerHistorialPorCliente(Long clienteId) {
        return pagoRepository.findByClienteId(clienteId);
    }
}