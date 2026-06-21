package com.gamebakes.serviciousuarios.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    @Test
    void testValoresEnum() {
        assertEquals("CLIENTE", Rol.CLIENTE.name());
        assertEquals("VENDEDOR", Rol.VENDEDOR.name());
    }

    @Test
    void testCantidadValores() {
        Rol[] roles = Rol.values();
        assertEquals(2, roles.length);
    }

    @Test
    void testValorOf() {
        assertEquals(Rol.CLIENTE, Rol.valueOf("CLIENTE"));
        assertEquals(Rol.VENDEDOR, Rol.valueOf("VENDEDOR"));
    }

    @Test
    void testValorOfInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            Rol.valueOf("ADMIN");
        });
    }
}
