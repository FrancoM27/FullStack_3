package com.gamebakes.servicio_perfil.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerfilTest {

    private Perfil perfil;

    @BeforeEach
    void setUp() {
        perfil = new Perfil();
    }

    @Test
    void testGettersAndSetters() {
        perfil.setIdPerfil(1L);
        perfil.setUsuarioId(100L);
        perfil.setNombreCompleto("Juan Pérez");
        perfil.setTelefono("123456789");
        perfil.setDireccion("Calle 123");

        assertEquals(1L, perfil.getIdPerfil());
        assertEquals(100L, perfil.getUsuarioId());
        assertEquals("Juan Pérez", perfil.getNombreCompleto());
        assertEquals("123456789", perfil.getTelefono());
        assertEquals("Calle 123", perfil.getDireccion());
    }

    @Test
    void testConstructorVacio() {
        Perfil perfilVacio = new Perfil();
        assertNotNull(perfilVacio);
    }

    @Test
    void testCamposNulos() {
        perfil.setIdPerfil(null);
        perfil.setUsuarioId(null);
        perfil.setNombreCompleto(null);
        perfil.setTelefono(null);
        perfil.setDireccion(null);

        assertNull(perfil.getIdPerfil());
        assertNull(perfil.getUsuarioId());
        assertNull(perfil.getNombreCompleto());
        assertNull(perfil.getTelefono());
        assertNull(perfil.getDireccion());
    }

    @Test
    void testTelefonoVacio() {
        perfil.setTelefono("");
        assertEquals("", perfil.getTelefono());
    }

    @Test
    void testDireccionVacia() {
        perfil.setDireccion("");
        assertEquals("", perfil.getDireccion());
    }
}
