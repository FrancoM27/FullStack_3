package com.gamebakes.serviciousuarios.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotBlank(message = "Debes ingresar tu usuario o correo electrónico")
    private String identifier;

    @NotBlank(message = "Este campo es obligatorio")
    private String password;

}
