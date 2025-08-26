package com.eduardo.stockify.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.eduardo.stockify.models.Usuario;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final String secret;

    public TokenService(@Qualifier("jwtSecret") String secret) {
        this.secret = secret;
    }

    public String gerarAccessToken(String username) {
        return gerarToken(username, dataExpiracaoAccessToken(), "access");
    }

    public String gerarRefreshToken(String username) {
        return gerarToken(username, dataExpiracaoRefreshToken(), "refresh");
    }

    public String gerarToken(String usuario, Instant expiraEm, String type) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Stockify")
                    .withSubject(usuario)
                    .withClaim("type", type)
                    .withExpiresAt(expiraEm)
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar Token JWT", exception);
        }
    }

    public String getSubject(String token, String expectedType) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            var decoded = JWT.require(algoritmo)
                    .withIssuer("API Stockify")
                    .build()
                    .verify(token);

            String type = decoded.getClaim("type").asString();
            if (!expectedType.equals(type)) {
                throw new JWTVerificationException("Tipo de token inválido!");
            }

            return decoded.getSubject();

        } catch (Exception ex) {
            throw new JWTVerificationException("Token JWT inválido ou expirado");
        }
    }

    private Instant dataExpiracaoAccessToken() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant dataExpiracaoRefreshToken() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));
    }

}
