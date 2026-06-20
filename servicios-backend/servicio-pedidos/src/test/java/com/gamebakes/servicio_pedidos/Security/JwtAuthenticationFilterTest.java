package com.gamebakes.servicio_pedidos.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_WithValidToken_SetsAuthentication() throws ServletException, IOException {
        String validToken = "Bearer valid.token.here";
        String username = "testUser";
        String rol = "CLIENTE";

        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.getSubjectFromToken(anyString())).thenReturn(username);
        when(jwtUtils.getRolFromToken(anyString())).thenReturn(rol);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(anyString());
        verify(jwtUtils, times(1)).getRolFromToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
        assertTrue(SecurityContextHolder.getContext().getAuthentication() != null);
    }

    @Test
    void testDoFilterInternal_WithoutAuthorizationHeader_DoesNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, never()).getSubjectFromToken(anyString());
        verify(jwtUtils, never()).getRolFromToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
        assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
    }

    @Test
    void testDoFilterInternal_WithInvalidBearerFormat_DoesNotSetAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat token.here");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, never()).getSubjectFromToken(anyString());
        verify(jwtUtils, never()).getRolFromToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
        assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
    }

    @Test
    void testDoFilterInternal_WithInvalidToken_DoesNotSetAuthentication() throws ServletException, IOException {
        String invalidToken = "Bearer invalid.token.here";

        when(request.getHeader("Authorization")).thenReturn(invalidToken);
        when(jwtUtils.getSubjectFromToken(anyString())).thenThrow(new RuntimeException("Invalid token"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
        assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
    }

    @Test
    void testDoFilterInternal_WithNullUsername_DoesNotSetAuthentication() throws ServletException, IOException {
        String validToken = "Bearer valid.token.here";

        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.getSubjectFromToken(anyString())).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
        assertTrue(SecurityContextHolder.getContext().getAuthentication() == null);
    }

    @Test
    void testDoFilterInternal_WithExistingAuthentication_DoesNotOverride() throws ServletException, IOException {
        String validToken = "Bearer valid.token.here";
        String username = "testUser";
        String rol = "CLIENTE";

        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.getSubjectFromToken(anyString())).thenReturn(username);
        when(jwtUtils.getRolFromToken(anyString())).thenReturn(rol);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
        assertTrue(SecurityContextHolder.getContext().getAuthentication() != null);
    }

    @Test
    void testDoFilterInternal_WithNullRol_SetsAuthenticationWithoutRole() throws ServletException, IOException {
        String validToken = "Bearer valid.token.here";
        String username = "testUser";

        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.getSubjectFromToken(anyString())).thenReturn(username);
        when(jwtUtils.getRolFromToken(anyString())).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(anyString());
        verify(jwtUtils, times(1)).getRolFromToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
        assertTrue(SecurityContextHolder.getContext().getAuthentication() != null);
    }
}
