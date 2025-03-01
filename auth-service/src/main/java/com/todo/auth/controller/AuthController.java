package com.todo.auth.controller;

import com.todo.auth.dtos.UserLoginRequestDTO;
import com.todo.auth.dtos.UserRegistrationRequestDTO;
import com.todo.auth.services.UserService;
import com.todo.dtos.UserDTO;
import com.todo.auth.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping("/login")
    public String login(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        UserDTO user = userService.loginUser(userLoginRequestDTO);
        return jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRoles().stream().map(String::valueOf).collect(Collectors.toSet()));
    }

    @GetMapping("/register")
    public UserDTO register(@RequestBody UserRegistrationRequestDTO userRegistrationRequestDTO) {
        return userService.registerUser(userRegistrationRequestDTO);
    }
    
}
