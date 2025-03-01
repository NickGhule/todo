package com.todo.auth.services;

import com.todo.auth.dtos.UserLoginRequestDTO;
import com.todo.auth.dtos.UserRegistrationRequestDTO;
import com.todo.auth.dtos.UserUpdateRequestDTO;
import com.todo.auth.exceptions.InvalidPasswordException;
import com.todo.auth.exceptions.UserNotFoundException;
import com.todo.auth.mapper.UserDTOMapper;
import com.todo.auth.models.User;
import com.todo.auth.repository.UserRepository;
import com.todo.dtos.UserDTO;
import com.todo.enums.RoleName;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public void changePassword(String userEmail, String oldPassword, String newPassword)  {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));
        String encodedOldPassword = passwordEncoder.encode(oldPassword);
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        if (!user.getPassword().equals(encodedOldPassword)){
            throw new InvalidPasswordException("Old Password does not match");
        }
        if (encodedNewPassword.equals(encodedOldPassword)){
            throw new InvalidPasswordException("New Password same as old password");
        }
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }

    @Transactional
    public UserDTO forceUpdatePassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        System.out.println(newPassword);
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        System.out.println(encodedNewPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
        return UserDTOMapper.toDto(user);
    }

    @Transactional
    public UserDTO registerUser(UserRegistrationRequestDTO registrationRequestDTO) {
        User user = new User();
        user.setEmail(registrationRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequestDTO.getPassword()));
        user.setName(registrationRequestDTO.getFirstName() + " " + registrationRequestDTO.getLastName());
        user.setRoles(Set.of(RoleName.USER));
        userRepository.save(user);

        return UserDTOMapper.toDto(user);
    }

    public UserDTO loginUser(UserLoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found with email: " + loginRequestDTO.getEmail()));
        String encodedPassword = passwordEncoder.encode(loginRequestDTO.getPassword());
        System.out.println(encodedPassword);
        System.out.println(user.getPassword());
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())){
            throw new InvalidPasswordException("Password does not match");
        }
        return UserDTOMapper.toDto(user);
    }

    public Set<RoleName> getRolesForUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return user.getRoles();
    }

    public UserDTO getUserDetailsByEmail(String email) {
        User user =  userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return UserDTOMapper.toDto(user);
    }

    public UserDTO updateUser(UserUpdateRequestDTO userUpdateDTO) {
        User user = userRepository.findById(userUpdateDTO.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userUpdateDTO.getUserId()));
        user.setName(userUpdateDTO.getFirstName() + " " + userUpdateDTO.getLastName());
        user.setEmail(userUpdateDTO.getEmail());
//        user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        userRepository.save(user);
        return UserDTOMapper.toDto(user);
    }
}
