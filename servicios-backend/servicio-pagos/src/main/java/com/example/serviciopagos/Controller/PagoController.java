package com.example.serviciopagos.Controller;

import com.example.serviciopagos.DTO.SolicitudPagoDTO;
import com.example.serviciopagos.Model.Pago;
import com.example.serviciopagos.Security.JwtAuthenticationFilter;
import com.example.serviciopagos.Service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "http://localhost:5173")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping("/iniciar")
    public ResponseEntity<Pago> iniciar(@RequestBody SolicitudPagoDTO solicitud, Authentication authentication){
        Long usuarioId = Long.parseLong(authentication.getName());
        solicitud.setClienteId(usuarioId);
        return ResponseEntity.ok(pagoService.iniciarPagoMP(solicitud));
    }

    @PostMapping("/confirmar/{idPago}")
    public ResponseEntity<Pago> confirmar(@PathVariable Long idPago, Authentication authentication) {
        Long usuarioId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(pagoService.confirmarPago(idPago, usuarioId));
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Pago>> historialPropio(Authentication authentication) {
        Long usuarioId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(pagoService.obtenerHistorialPorCliente(usuarioId));
    }
}
