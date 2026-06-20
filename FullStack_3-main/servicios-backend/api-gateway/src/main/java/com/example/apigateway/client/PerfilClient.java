package com.example.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
    name = "servicio-perfil",
    url = "http://18.205.233.123:8084/api/perfil"
)
public interface PerfilClient {

    @GetMapping("/usuario/{usuarioId}")
    Map<String, Object> obtenerPerfil(@PathVariable("usuarioId") Long usuarioId);

    @PostMapping
    Map<String, Object> crearPerfil(@RequestBody Map<String, Object> perfil);

    @PutMapping("/usuario/{usuarioId}")
    Map<String, Object> actualizarPerfil(@PathVariable("usuarioId") Long usuarioId, @RequestBody Map<String, Object> perfil);
}
