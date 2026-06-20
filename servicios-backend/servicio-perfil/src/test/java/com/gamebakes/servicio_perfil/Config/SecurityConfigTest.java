package com.gamebakes.servicio_perfil.Config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private HttpSecurity httpSecurity;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void securityFilterChain_ConfiguracionCorrecta() throws Exception {
        HttpSecurity httpSecurityMock = mock(HttpSecurity.class);
        DefaultSecurityFilterChain defaultSecurityFilterChain = mock(DefaultSecurityFilterChain.class);

        when(httpSecurityMock.csrf(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.cors(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.sessionManagement(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.authorizeHttpRequests(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.build()).thenReturn(defaultSecurityFilterChain);

        SecurityFilterChain result = securityConfig.securityFilterChain(httpSecurityMock);

        assertNotNull(result);
        verify(httpSecurityMock, times(1)).csrf(any());
        verify(httpSecurityMock, times(1)).cors(any());
        verify(httpSecurityMock, times(1)).sessionManagement(any());
        verify(httpSecurityMock, times(1)).authorizeHttpRequests(any());
        verify(httpSecurityMock, times(1)).build();
    }

    @Test
    void securityFilterChain_CsrfDeshabilitado() throws Exception {
        HttpSecurity httpSecurityMock = mock(HttpSecurity.class);
        DefaultSecurityFilterChain defaultSecurityFilterChain = mock(DefaultSecurityFilterChain.class);

        when(httpSecurityMock.csrf(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.cors(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.sessionManagement(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.authorizeHttpRequests(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.build()).thenReturn(defaultSecurityFilterChain);

        securityConfig.securityFilterChain(httpSecurityMock);

        verify(httpSecurityMock, times(1)).csrf(any());
    }

    @Test
    void securityFilterChain_CorsConfigurado() throws Exception {
        HttpSecurity httpSecurityMock = mock(HttpSecurity.class);
        DefaultSecurityFilterChain defaultSecurityFilterChain = mock(DefaultSecurityFilterChain.class);

        when(httpSecurityMock.csrf(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.cors(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.sessionManagement(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.authorizeHttpRequests(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.build()).thenReturn(defaultSecurityFilterChain);

        securityConfig.securityFilterChain(httpSecurityMock);

        verify(httpSecurityMock, times(1)).cors(any());
    }

    @Test
    void securityFilterChain_SesionStateless() throws Exception {
        HttpSecurity httpSecurityMock = mock(HttpSecurity.class);
        DefaultSecurityFilterChain defaultSecurityFilterChain = mock(DefaultSecurityFilterChain.class);

        when(httpSecurityMock.csrf(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.cors(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.sessionManagement(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.authorizeHttpRequests(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.build()).thenReturn(defaultSecurityFilterChain);

        securityConfig.securityFilterChain(httpSecurityMock);

        verify(httpSecurityMock, times(1)).sessionManagement(any());
    }

    @Test
    void securityFilterChain_AutorizacionPermitAll() throws Exception {
        HttpSecurity httpSecurityMock = mock(HttpSecurity.class);
        DefaultSecurityFilterChain defaultSecurityFilterChain = mock(DefaultSecurityFilterChain.class);

        when(httpSecurityMock.csrf(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.cors(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.sessionManagement(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.authorizeHttpRequests(any())).thenReturn(httpSecurityMock);
        when(httpSecurityMock.build()).thenReturn(defaultSecurityFilterChain);

        securityConfig.securityFilterChain(httpSecurityMock);

        verify(httpSecurityMock, times(1)).authorizeHttpRequests(any());
    }
}
