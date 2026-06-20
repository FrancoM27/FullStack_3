package com.gamebakes.servicio_perfil.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolTest {

    @Test
    void testValoresEnum() {
        assertEquals("CLIENTE", Rol.CLIENTE.name());
        assertEquals("VENDEDOR", Rol.VENDEDOR.name());
        assertEquals("USUARIO", Rol.USUARIO.name());
        assertEquals("ADMIN", Rol.ADMIN.name());
    }

    @Test
    void testEnumValues() {
        Rol[] roles = Rol.values();
        assertEquals(4, roles.length);
        assertEquals(Rol.CLIENTE, roles[0]);
        assertEquals(Rol.VENDEDOR, roles[1]);
        assertEquals(Rol.USUARIO, roles[2]);
        assertEquals(Rol.ADMIN, roles[3]);
    }

    @Test
    void testValueOf() {
        assertEquals(Rol.CLIENTE, Rol.valueOf("CLIENTE"));
        assertEquals(Rol.VENDEDOR, Rol.valueOf("VENDEDOR"));
        assertEquals(Rol.USUARIO, Rol.valueOf("USUARIO"));
        assertEquals(Rol.ADMIN, Rol.valueOf("ADMIN"));
    }

    @Test
    void testValueOfInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            Rol.valueOf("INVALIDO");
        });
    }
}
