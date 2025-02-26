package com.todo.gateway.filters;

import com.todo.gateway.security.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


/**
 * A web filter that intercepts HTTP requests to enforce JWT-based authentication.
 *
 * <p>This filter bypasses requests directed towards authentication endpoints (paths containing "/auth")
 * and validates the JWT token provided in the "Authorization" header for other requests. The token is
 * expected to be prefixed with "Bearer " and is verified using the provided {@code JwtUtil} instance.
 *
 * <p>If the token is absent, empty, or invalid, the filter responds with a 401 Unauthorized status,
 * otherwise, the request is passed along the filter chain.
 *
 * @see JwtUtil
 */
@Component
public class AuthFilter implements WebFilter {

    JwtUtil jwtUtil;

    public AuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        if (request.getURI().getPath().contains("/auth")) {
            return chain.filter(exchange);
        }

        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token == null || token.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " (7 characters)
        }

        if(!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
