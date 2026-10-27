package com.aula.pos.appinscricao.service.impl;

import com.aula.pos.appinscricao.exception.TokenException;
import com.aula.pos.appinscricao.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("app-inscricoes")
                    .withSubject(usuario.getId().toString())
                    .withClaim("email", usuario.getEmail())
                    .withClaim("role", usuario.getRole().toString())
                    .withExpiresAt(expires())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new TokenException("Error ao gerar Token");
        }
    }

    private Instant expires() {
        return LocalDateTime.now().plusMinutes(30).atZone(ZoneId.of("-03:00")).toInstant();
    }

}
