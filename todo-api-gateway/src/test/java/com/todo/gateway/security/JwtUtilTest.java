package com.todo.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String SECRET_KEY = "your-secret-key-which-should-be-very-long-and-secure";
    private SecretKey secretKey;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtil();
        secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    @Test
    public void testGenerateToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    public void testValidateToken_withValidToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    public void testValidateToken_withInvalidToken() {
        String token = "invalidToken";

        assertFalse(jwtUtil.validateToken(token));
    }

    @Test
    public void testGetClaims_withValidToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        Claims claims = jwtUtil.getClaims(token);

        assertNotNull(claims);
        assertEquals(username, claims.getSubject());
    }

    @Test
    public void testGetClaims_withInvalidToken() {
        String token = "invalidToken";

        Claims claims = jwtUtil.getClaims(token);

        assertNull(claims);
    }
}