package com.gamebakes.serviciousuarios.Repository;

import com.gamebakes.serviciousuarios.Model.PasswordResetToken;
import com.gamebakes.serviciousuarios.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUsuario(Usuario usuario);
}
