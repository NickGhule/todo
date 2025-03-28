package com.todo.auth.config;

import com.todo.auth.services.UserService;
import com.todo.security.JwtUtil;
import com.todo.dtos.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public OAuth2AuthenticationSuccessHandler(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        UserDTO userDTO = userService.getUserDetailsByEmail(email);
        Set<String> roles = userDTO.getRoles()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());

        String token = jwtUtil.generateToken(userDTO.getUserId(), userDTO.getEmail(), roles);
//        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        Map<String, String> tokenData = new HashMap<>();
        tokenData.put("token", token);
        response.getWriter().write(
                tokenData.toString()
        );

    }
}
