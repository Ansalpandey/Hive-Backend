package com.squareup.digital.controller;


import com.squareup.digital.dto.LoginRequest;
import com.squareup.digital.dto.LoginResponse;
import com.squareup.digital.dto.ProfileResponse;
import com.squareup.digital.model.UserModel;
import com.squareup.digital.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String index() {
        return "Welcome to the Auth API!";
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody UserModel request) {
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.loginUser(request);
    }

    @GetMapping("/users/{username}")
    public ProfileResponse getProfile(@PathVariable String username) {
        return authService.getProfile(username);
    }
}
