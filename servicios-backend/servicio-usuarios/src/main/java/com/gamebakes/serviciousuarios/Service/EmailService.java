package com.gamebakes.serviciousuarios.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoRecuperacion(String email, String token){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de Contraseña - Gamebakes");

        String url = "http://localhost:5173/reset-password?token=" + token;

        message.setText("Hola Guerrero .\n\n" +
                "Has solicitado reestablecer tu contraseña. Haz clic en el siguiente enlace para continuar:\n" +
                url + "\n\n" +
                "Este enlace expirará en 15 minutos.");

        mailSender.send(message);
    }
}
