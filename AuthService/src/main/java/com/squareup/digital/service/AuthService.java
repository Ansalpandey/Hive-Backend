package com.squareup.digital.service;

import com.squareup.digital.dto.LoginRequest;
import com.squareup.digital.dto.LoginResponse;
import com.squareup.digital.model.UserModel;
import com.squareup.digital.repository.AuthRepository;
import com.squareup.digital.utils.JwtUtil;
import com.squareup.digital.validators.AuthValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthValidator authValidator;
    private final JwtUtil jwtUtil;

    public AuthService(AuthRepository authRepository,
                       PasswordEncoder passwordEncoder, AuthValidator authValidator,
                       JwtUtil jwtUtil) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.authValidator = authValidator;
        this.jwtUtil = jwtUtil;
    }

    // =========================
    // REGISTER
    // =========================
    @Transactional
    public String registerUser(UserModel user) {

        if (authRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        authValidator.validateUser(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        authRepository.save(user);

        return "User registered successfully";
    }

    // =========================
    // LOGIN
    // =========================
    public LoginResponse loginUser(LoginRequest loginRequest) {

        if (loginRequest.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        UserModel user = authRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(loginRequest);

        return new LoginResponse(token, user);
    }

    @Transactional
    public LoginResponse processGoogleLogin(OAuth2User principal) {

        String email = principal.getAttribute("email");
        if (email == null) {
            throw new IllegalArgumentException("Google account does not have an associated email.");
        }

        String firstName = principal.getAttribute("given_name");
        String lastName = principal.getAttribute("family_name");
        String profileImage = principal.getAttribute("picture");

        UserModel user = authRepository.findByUsername(email)
                .orElseGet(() -> {
                    UserModel newUser = new UserModel();
                    newUser.setUsername(email);
                    newUser.setEmail(email);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setProfile(profileImage);
                    newUser.setPassword("");

                    return authRepository.save(newUser);
                });

        String token = jwtUtil.generateToken(
                new LoginRequest(user.getUsername(), "")
        );

        return new LoginResponse(token, user);
    }
}