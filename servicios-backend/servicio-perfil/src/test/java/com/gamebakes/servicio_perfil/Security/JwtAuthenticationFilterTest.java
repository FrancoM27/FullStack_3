package com.gamebakes.servicio_perfil.Security;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

//import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private String validToken;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        validToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbCI6IkNMSUVOVEUifQ.test";
    }

    @Test
    void doFilterInternal_TokenValido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.getSubjectFromToken(any())).thenReturn("testuser");
        when(jwtUtils.getRolFromToken(any())).thenReturn("CLIENTE");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(any());
        verify(jwtUtils, times(1)).getRolFromToken(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_TokenInvalido() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid_token");
        when(jwtUtils.getSubjectFromToken(any())).thenThrow(new RuntimeException("Invalid token"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_SinHeaderAuthorization() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, never()).getSubjectFromToken(any());
        verify(jwtUtils, never()).getRolFromToken(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_HeaderMalformado() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat token");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, never()).getSubjectFromToken(any());
        verify(jwtUtils, never()).getRolFromToken(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_HeaderSinBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("token_sin_bearer");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, never()).getSubjectFromToken(any());
        verify(jwtUtils, never()).getRolFromToken(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ConAutenticacionExistente() throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.getSubjectFromToken(any())).thenReturn("testuser");
        when(jwtUtils.getRolFromToken(any())).thenReturn("CLIENTE");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(any());
        verify(jwtUtils, times(1)).getRolFromToken(any());
        verify(filterChain, times(1)).doFilter(request, response);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_SubjectNull() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.getSubjectFromToken(any())).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ExcepcionEnJwtUtils() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(validToken);
        when(jwtUtils.getSubjectFromToken(any())).thenThrow(new RuntimeException("JWT parsing error"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtils, times(1)).getSubjectFromToken(any());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
