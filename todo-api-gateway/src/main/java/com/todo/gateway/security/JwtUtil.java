package com.todo.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {


    // TODO: Get the secret key from the environment variable
    private final String SECRET_KEY = "your-secret-key-which-should-be-very-long-and-secure";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String HEADER_STRING = "Authorization";
    private final long EXPIRATION_TIME = 1000*60*60; // 1 hour

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String usernamne) {
        return Jwts.builder()
                .subject(usernamne)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return validateToken(token.substring(TOKEN_PREFIX.length()));
        }
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey()).build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }


    public Claims getClaims(String token) {
        if(validateToken(token)) {
            if(token.startsWith(TOKEN_PREFIX)) {
                token = token.substring(TOKEN_PREFIX.length());
            }
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        return null;
    }
}
