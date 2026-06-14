package com.gamebakes.serviciousuarios.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    @NotBlank(message = "El nombre de usuario no puede estar vacio")
    private String username;

    @NotBlank(message = "El campo de nombre no puede estar vacio")
    private String nombreCompleto;

    @Email(message = "Debe ser un formato de email válido")
    @NotBlank(message = "El campo de email no puede estar vacío")
    private String email;
}
