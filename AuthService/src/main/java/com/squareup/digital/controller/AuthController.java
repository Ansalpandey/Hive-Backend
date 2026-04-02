package com.squareup.digital.controller;


import com.squareup.digital.dto.LoginRequest;
import com.squareup.digital.dto.LoginResponse;
import com.squareup.digital.model.UserModel;
import com.squareup.digital.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =========================
    // HEALTH / TEST ENDPOINT
    // =========================
    @GetMapping
    public String index() {
        return "Welcome to the Auth API!";
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public String register(@Valid @RequestBody UserModel request) {
        return authService.registerUser(request);
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.loginUser(request);
    }

    // =========================
    // GOOGLE OAUTH SUCCESS
    // =========================
    @GetMapping("/google/success")
    public LoginResponse googleLogin(@AuthenticationPrincipal OAuth2User principal) {
        return authService.processGoogleLogin(principal);
    }
}
