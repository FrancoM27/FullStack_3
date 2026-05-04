package com.gamebakes.serviciousuarios.DTO;

import com.gamebakes.serviciousuarios.Model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroDTO {

    @NotBlank(message = "Este campo es obligatorio")
    private String username;

    @NotBlank(message = "Este campo es obligatorio")
    private String password;

    @Email(message = "Ingresa un formato de email válido")
    @NotBlank(message = "Este campo es obligatorio")
    private String email;

    @NotBlank(message = "Este campo es obligatorio")
    private String nombreCompleto;

    private String rol;

}
