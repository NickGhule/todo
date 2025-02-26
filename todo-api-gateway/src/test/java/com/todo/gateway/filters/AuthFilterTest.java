package com.todo.gateway.filters;

import com.todo.gateway.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class AuthFilterTest {

    private JwtUtil jwtUtil;
    private AuthFilter authFilter;
    private WebFilterChain chain;

    @BeforeEach
    public void setUp() {
        // Mock the JwtUtil dependency
        jwtUtil = mock(JwtUtil.class);
        // Initialize the AuthFilter with the mocked JwtUtil
        authFilter = new AuthFilter(jwtUtil);
        // Mock the WebFilterChain
        chain = mock(WebFilterChain.class);
        // Simulate filter chain processing by returning an empty Mono
        when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    public void testFilter_withAuthPath_shouldBypassFilter() {
        // Create a mock exchange for the /auth/login path
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/auth/login").build());

        // Call the filter method of AuthFilter
        Mono<Void> result = authFilter.filter(exchange, chain);

        // Verify that the filter completes without errors
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        // Verify that the filter chain was called once
        verify(chain, times(1)).filter(exchange);
        // Verify that JwtUtil was not interacted with
        verifyNoInteractions(jwtUtil);
    }

    @Test
    public void testFilter_withNoToken_shouldReturnUnauthorized() {
        // Create a mock exchange for a path that requires authentication but without a token
        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/resource").build());

        // Call the filter method of AuthFilter
        Mono<Void> result = authFilter.filter(exchange, chain);

        // Verify that the filter completes without errors
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        // Verify that the response status is set to UNAUTHORIZED
        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;

        // Verify that the filter chain was never called
        verify(chain, never()).filter(exchange);
    }

    @Test
    public void testFilter_withInvalidToken_shouldReturnUnauthorized() {
        // Create a mock exchange for a path that requires authentication with an invalid token
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/resource")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token")
                        .build()
        );

        // Mock the JwtUtil to return false for the invalid token
        when(jwtUtil.validateToken("invalid_token")).thenReturn(false);

        // Call the filter method of AuthFilter
        Mono<Void> result = authFilter.filter(exchange, chain);

        // Verify that the filter completes without errors
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        // Verify that the response status is set to UNAUTHORIZED
        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
        // Verify that the filter chain was never called
        verify(chain, never()).filter(exchange);
    }

    @Test
    public void testFilter_withValidToken_shouldPassFilter() {
        // Create a mock exchange for a path that requires authentication with a valid token
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/resource")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
                        .build()
        );

        // Mock the JwtUtil to return true for the valid token
        when(jwtUtil.validateToken("valid_token")).thenReturn(true);

        // Call the filter method of AuthFilter
        Mono<Void> result = authFilter.filter(exchange, chain);

        // Verify that the filter completes without errors
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        // Verify that the filter chain was called once
        verify(chain, times(1)).filter(exchange);
    }
}