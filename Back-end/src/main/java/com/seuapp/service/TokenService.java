package com.seuapp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.seuapp.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private String secret = "uiuiuaiai-secreta-sxxxx-segura-123";

    public String gerarToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("Federado-App") // Nome da sua aplicação
                    .withSubject(user.getEmail()) // O dado que identifica o usuário (geralmente e-mail ou ID)
                    .withExpiresAt(gerarDataExpiracao()) // Tempo de validade
                    .sign(algorithm); // Assina o token com a chave secreta
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("fed-app-testes")
                    .build()
                    .verify(token)
                    .getSubject(); // Retorna o e-mail do usuário se o token for válido
        } catch (JWTVerificationException exception) {
            return ""; // Retorna string vazia se o token for inválido, forjado ou expirado
        }
    }

    private Instant gerarDataExpiracao() {
        // Define que o token expira em 2 horas. O fuso horário -03:00 é o de Brasília.
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}