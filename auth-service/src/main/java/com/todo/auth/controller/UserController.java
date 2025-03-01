package com.todo.auth.controller;

import com.todo.auth.dtos.UpdatePasswordRequestDTO;
import com.todo.auth.dtos.UserRegistrationRequestDTO;
import com.todo.auth.dtos.UserUpdateRequestDTO;
import com.todo.auth.models.User;
import com.todo.auth.services.UserService;
import com.todo.dtos.UserDTO;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getUserDetails(
            @RequestParam UUID userId,
            @RequestParam String email
    ) {
        UserDTO user = userService.getUserDetailsByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserUpdateRequestDTO userUpdateDTO, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaim("userId");
        if(userId == null || !userId.equals(userUpdateDTO.getUserId().toString())) {
            throw new InvalidBearerTokenException("Token does not belong to user " + userUpdateDTO.getUserId().toString());
        }
        UserDTO user = userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<UserDTO> updatePassword(@RequestBody  UpdatePasswordRequestDTO updatePasswordRequestDTO,
                                                  @AuthenticationPrincipal Jwt jwt ) {
        String userId = jwt.getClaim("userId");
        assert userId != null;
        UserDTO user = userService.forceUpdatePassword(UUID.fromString(userId), updatePasswordRequestDTO.getNewPassword());
        return ResponseEntity.ok(user);
    }
}
