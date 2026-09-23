package com.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.model.User;
import com.service.AuthService;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ==========================
    // SIGNUP
    // ==========================

    @PostMapping("/auth/signup")
    public Map<String, Object> signup(
            @RequestBody User user) {

        return authService.signup(user);
    }

    // ==========================
    // LOGIN
    // ==========================

    @PostMapping("/auth/login")
    public Map<String, Object> login(
            @RequestBody Map<String, String> request) {

        String username =
                request.get("username");

        String password =
                request.get("password");

        return authService.login(
                username,
                password
        );
    }

    // ==========================
    // NORMAL PROTECTED ENDPOINT
    // ==========================

    @GetMapping("/auth/home")
    public String home() {

        return "Welcome to SpendWise Authentication Service";
    }
}