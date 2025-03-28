package com.todo.auth.config;

import com.todo.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2AuthenticationSuccessHandler successHandler;
    private final JwtUtil jwtUtil;

    public SecurityConfig(OAuth2AuthenticationSuccessHandler successHandler, JwtUtil jwtUtil) {
        this.successHandler = successHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/", "/error/**").permitAll() // Open authentication endpoints
                        .anyRequest().authenticated()
                ).oauth2Login(oauth2 -> oauth2
                        .successHandler(successHandler)
//                        .authorizationEndpoint(endpoint ->
//                                endpoint.baseUri("/oauth2/authorize"))
//                        .redirectionEndpoint(endpoint ->
//                                endpoint.baseUri("/oauth2/callback")) // Redirect on successful login

                ) // OAuth2 login
//                .formLogin(withDefaults()) // Support form login for testing

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                ;

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withSecretKey(jwtUtil.getSecretKey())
                .macAlgorithm(MacAlgorithm.HS384)
                .build();
    }

}
