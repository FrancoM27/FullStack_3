package com.gamebakes.serviciousuarios.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        // Le inyectamos el valor que el test espera encontrar
        ReflectionTestUtils.setField(emailService, "frontendUrl", "http://localhost:5173");
    }

    @Test
    void enviarCorreoRecuperacion_Exito() {
        String email = "test@example.com";
        String token = "token123";

        emailService.enviarCorreoRecuperacion(email, token);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertEquals(email, capturedMessage.getTo()[0]);
        assertEquals("Recuperación de Contraseña - Gamebakes", capturedMessage.getSubject());
        assertTrue(capturedMessage.getText().contains(token));
        // Ahora sí coincidirá con el valor inyectado en el setUp
        assertTrue(capturedMessage.getText().contains("http://localhost:5173/reset-password?token="));
    }

    @Test
    void enviarCorreoRecuperacion_VerificaContenido() {
        String email = "test@example.com";
        String token = "token123";

        emailService.enviarCorreoRecuperacion(email, token);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        String text = capturedMessage.getText();

        assertTrue(text.contains("Hola Guerrero"));
        assertTrue(text.contains("Has solicitado reestablecer tu contraseña"));
        assertTrue(text.contains("Este enlace expirará en 15 minutos"));
    }
}