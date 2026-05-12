package com.example.serviciopagos.Service;

import com.example.serviciopagos.DTO.SolicitudPagoDTO;
import com.example.serviciopagos.Model.Pago;
import com.example.serviciopagos.Repository.PagoRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    private String accessToken = "APP_USR-6384651523153058-051023-18ce169c7c92f41fc1af6ae5d5ad9a39-3392426062";

    public Pago iniciarPagoMP(SolicitudPagoDTO solicitud){
        Pago pago = new Pago();
        pago.setPedidoId(solicitud.getPedidoId());
        pago.setClienteId(solicitud.getClienteId());
        pago.setMonto(solicitud.getMonto());
        pago.setMetodoPago("MERCADO_PAGO");
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("PENDIENTE");
        pago = pagoRepository.save(pago);

        try{
            MercadoPagoConfig.setAccessToken(accessToken);

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title("Pedido Gamebakes #" + solicitud.getPedidoId())
                    .quantity(1)
                    .unitPrice(new BigDecimal(solicitud.getMonto()))
                    .build();

            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:5173/pago-exito")
                    .failure("http://localhost:5173/pago-fallido")
                    .pending("http://localhost:5173/pago-pendiente")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .externalReference(pago.getIdPago().toString())
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            pago.setTransaccionId(preference.getInitPoint());
            return  pagoRepository.save(pago);
        } catch(Exception e){
            throw new RuntimeException("Error con Mercado Pago: "+ e.getMessage());
        }
    }

    public Pago confirmarPago(Long idPago, Long usuarioAutenticadoId){
        Pago pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Error: Pago no ecnontrado"));

        if (!pago.getClienteId().equals(usuarioAutenticadoId)) {
            throw new RuntimeException("No autorizado para realizar este pago");
        }

        pago.setEstado("APROBADO");
        pago.setTransaccionId("MP-CONFIRM-"+ UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return pagoRepository.save(pago);
    }

    public List<Pago> obtenerHistorialPorCliente(Long clienteId) {
        return pagoRepository.findByClienteId(clienteId);
    }

}
