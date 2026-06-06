package com.example.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final String secretKeyStr = "GameBakes_Secret_Key_2026_No_Compartir";

    public AuthenticationFilter(){
        super(Config.class);
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config){
        return (exchange, chain) ->{
            ServerHttpRequest request = exchange.getRequest();

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            System.out.println("=== AUTHENTICATION FILTER ===");
            System.out.println("Path: " + request.getPath());
            System.out.println("AuthHeader: " + authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")){
                System.out.println("Error: Formato de token invalido");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Formato de token invalido");
            }

            String token = authHeader.substring(7);

            try{
                SecretKey key = Keys.hmacShaKeyFor(secretKeyStr.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String userId = claims.getSubject();
                String rol = claims.get("rol", String.class);

                System.out.println("UserId: " + userId);
                System.out.println("Rol: " + rol);

                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Role", rol)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (Exception e){
                System.out.println("Error validando token: " + e.getMessage());
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token falso o vencido");
            }
        };
    }
}