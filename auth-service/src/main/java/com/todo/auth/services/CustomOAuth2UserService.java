package com.todo.auth.services;

import com.todo.auth.enums.AuthProvider;
import com.todo.enums.RoleName;
import com.todo.auth.models.User;
import com.todo.auth.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String providerId = oAuth2User.getAttribute("sub"); // Google ID
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user;
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setProviderId(providerId);
            user.setProvider(AuthProvider.valueOf(provider));

            // Fetch the default USER role from DB
            user.setRoles(Set.of(RoleName.USER));

            userRepository.save(user);
        }

        // Convert roles to Spring Security authorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "sub");
    }
}
