package com.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.model.User;
import com.repo.UserRepo;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public Map<String, Object> signup(User user) {

        if (user.getUsername() == null ||
                user.getUsername().isBlank()) {

            throw new RuntimeException("Username is required");
        }

        if (user.getEmail() == null ||
                user.getEmail().isBlank()) {

            throw new RuntimeException("Email is required");
        }

        if (user.getPassword() == null ||
                user.getPassword().isBlank()) {

            throw new RuntimeException("Password is required");
        }

        if (userRepo.existsByUsername(user.getUsername())) {

            throw new RuntimeException(
                    "Username already exists");
        }

        if (userRepo.existsByEmail(user.getEmail())) {

            throw new RuntimeException(
                    "Email already exists");
        }

        // Encrypt password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Default role
        user.setRole("USER");

        // Default status
        user.setStatus("ACTIVE");

        User savedUser = userRepo.save(user);

        Map<String, Object> response = new HashMap<>();

        response.put(
                "message",
                "User registered successfully"
        );

        response.put(
                "userId",
                savedUser.getUserId()
        );

        response.put(
                "username",
                savedUser.getUsername()
        );

        response.put(
                "email",
                savedUser.getEmail()
        );

        response.put(
                "role",
                savedUser.getRole()
        );

        response.put(
                "status",
                savedUser.getStatus()
        );

        return response;
    }

    // ==========================
    // LOGIN
    // ==========================

    public Map<String, Object> login(
            String username,
            String password) {

        User user = userRepo
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"
                        )
                );

        // Check password
        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        // Check account status
        if (!"ACTIVE".equalsIgnoreCase(
                user.getStatus())) {

            throw new RuntimeException(
                    "User account is not active"
            );
        }

        // Generate JWT
        String token =
                jwtService.generateToken(user);

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "message",
                "Login successful"
        );

        response.put(
                "token",
                token
        );

        response.put(
                "userId",
                user.getUserId()
        );

        response.put(
                "username",
                user.getUsername()
        );

        response.put(
                "email",
                user.getEmail()
        );

        response.put(
                "role",
                user.getRole()
        );

        return response;
    }
}