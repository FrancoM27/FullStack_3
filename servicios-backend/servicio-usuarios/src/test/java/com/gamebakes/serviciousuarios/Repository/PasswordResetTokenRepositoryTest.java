package com.gamebakes.serviciousuarios.Repository;

import com.gamebakes.serviciousuarios.Model.PasswordResetToken;
import com.gamebakes.serviciousuarios.Model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenRepositoryTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    private PasswordResetToken token;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@example.com");

        token = new PasswordResetToken("token123", usuario);
        token.setId(1L);
    }

    @Test
    void findByToken_TokenEncontrado() {
        when(tokenRepository.findByToken("token123")).thenReturn(Optional.of(token));

        Optional<PasswordResetToken> result = tokenRepository.findByToken("token123");

        assertTrue(result.isPresent());
        assertEquals("token123", result.get().getToken());
        assertEquals(usuario, result.get().getUsuario());
        verify(tokenRepository, times(1)).findByToken("token123");
    }

    @Test
    void findByToken_TokenNoEncontrado() {
        when(tokenRepository.findByToken("nonexistent")).thenReturn(Optional.empty());

        Optional<PasswordResetToken> result = tokenRepository.findByToken("nonexistent");

        assertFalse(result.isPresent());
        verify(tokenRepository, times(1)).findByToken("nonexistent");
    }

    @Test
    void deleteByUsuario_Exito() {
        doNothing().when(tokenRepository).deleteByUsuario(any(Usuario.class));

        tokenRepository.deleteByUsuario(usuario);

        verify(tokenRepository, times(1)).deleteByUsuario(usuario);
    }

    @Test
    void save_TokenGuardado() {
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(token);

        PasswordResetToken result = tokenRepository.save(token);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("token123", result.getToken());
        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
    }

    @Test
    void delete_TokenEliminado() {
        doNothing().when(tokenRepository).delete(any(PasswordResetToken.class));

        tokenRepository.delete(token);

        verify(tokenRepository, times(1)).delete(token);
    }

    @Test
    void findById_TokenEncontrado() {
        when(tokenRepository.findById(1L)).thenReturn(Optional.of(token));

        Optional<PasswordResetToken> result = tokenRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(tokenRepository, times(1)).findById(1L);
    }

    @Test
    void findById_TokenNoEncontrado() {
        when(tokenRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<PasswordResetToken> result = tokenRepository.findById(999L);

        assertFalse(result.isPresent());
        verify(tokenRepository, times(1)).findById(999L);
    }
}
