package com.example.servicioproductos.Controller;

import com.example.servicioproductos.Model.Producto;
import com.example.servicioproductos.Service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.ServletException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Producto producto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productoController).build();
        objectMapper = new ObjectMapper();

        producto = new Producto();
        producto.setId(1L);
        producto.setVendedorId(100L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción del producto");
        producto.setPrecio(99.99);
        producto.setStock(50);
        producto.setImagenUrl("http://example.com/imagen.jpg");
        producto.setCategoria("Electrónica");
        producto.setActivo(true);
    }

    @Test
    void listar_Exito() throws Exception {
        List<Producto> productos = Arrays.asList(producto);
        when(productoService.listar()).thenReturn(productos);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Producto Test"))
                .andExpect(jsonPath("$[0].precio").value(99.99));

        verify(productoService, times(1)).listar();
    }

    @Test
    void listarPorVendedor_Exito() throws Exception {
        List<Producto> productos = Arrays.asList(producto);
        when(productoService.listarPorVendedor(100L)).thenReturn(productos);

        mockMvc.perform(get("/api/productos/vendedor/{vendedorId}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].vendedorId").value(100L));

        verify(productoService, times(1)).listarPorVendedor(100L);
    }

    @Test
    void crear_Exito() throws Exception {
        when(productoService.guardar(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto))
                        .header("X-User-Id", "100")
                        .header("X-User-Role", "VENDEDOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Producto Test"));

        verify(productoService, times(1)).guardar(any(Producto.class));
    }

    @Test
    void crear_RolNoAutorizado() throws Exception {
        assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/api/productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(producto))
                            .header("X-User-Id", "100")
                            .header("X-User-Role", "CLIENTE"))
                    .andReturn();
        });

        verify(productoService, never()).guardar(any(Producto.class));
    }

    @Test
    void obtenerPorId_Exito() throws Exception {
        when(productoService.obtenerPorId(1L)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Producto Test"));

        verify(productoService, times(1)).obtenerPorId(1L);
    }

    @Test
    void eliminar_Exito() throws Exception {
        doNothing().when(productoService).eliminar(1L);

        mockMvc.perform(delete("/api/productos/{id}", 1L)
                        .header("X-User-Role", "VENDEDOR"))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).eliminar(1L);
    }

    @Test
    void eliminar_RolNoAutorizado() throws Exception {
        assertThrows(ServletException.class, () -> {
            mockMvc.perform(delete("/api/productos/{id}", 1L)
                            .header("X-User-Role", "CLIENTE"))
                    .andReturn();
        });

        verify(productoService, never()).eliminar(any(Long.class));
    }

    @Test
    void cambiarEstado_Exito() throws Exception {
        producto.setActivo(false);
        when(productoService.cambiarEstadoActivo(1L, false)).thenReturn(producto);

        mockMvc.perform(patch("/api/productos/{id}/estado", 1L)
                        .param("activo", "false")
                        .header("X-User-Role", "VENDEDOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));

        verify(productoService, times(1)).cambiarEstadoActivo(1L, false);
    }

    @Test
    void cambiarEstado_RolNoAutorizado() throws Exception {
        mockMvc.perform(patch("/api/productos/{id}/estado", 1L)
                        .param("activo", "false")
                        .header("X-User-Role", "CLIENTE"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Acceso denegado")));

        verify(productoService, never()).cambiarEstadoActivo(any(Long.class), any(Boolean.class));
    }

    @Test
    void actualizar_Exito() throws Exception {
        when(productoService.guardar(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/productos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto))
                        .header("X-User-Id", "100")
                        .header("X-User-Role", "VENDEDOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(productoService, times(1)).guardar(any(Producto.class));
    }

    @Test
    void actualizar_RolNoAutorizado() throws Exception {
        assertThrows(ServletException.class, () -> {
            mockMvc.perform(put("/api/productos/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(producto))
                            .header("X-User-Id", "100")
                            .header("X-User-Role", "CLIENTE"))
                    .andReturn();
        });

        verify(productoService, never()).guardar(any(Producto.class));
    }

    @Test
    void restarStock_Exito() throws Exception {
        producto.setStock(40);
        when(productoService.restarStock(1L, 10)).thenReturn(producto);

        mockMvc.perform(put("/api/productos/{id}/restar-stock", 1L)
                        .param("cantidad", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(40));

        verify(productoService, times(1)).restarStock(1L, 10);
    }
}
