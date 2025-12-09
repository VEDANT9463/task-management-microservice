package com.user.service;

import com.user.entity.User;
import com.user.repository.UserRepository;
import com.user.roles.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User u = new User();
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRoles(Set.of(Role.ROLE_USER));

        return userRepository.save(u);
    }
}
