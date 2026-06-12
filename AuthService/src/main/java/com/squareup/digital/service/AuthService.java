package com.squareup.digital.service;

import com.squareup.digital.dto.LoginRequest;
import com.squareup.digital.dto.LoginResponse;
import com.squareup.digital.dto.ProfileResponse;
import com.squareup.digital.model.UserModel;
import com.squareup.digital.repository.AuthRepository;
import com.squareup.digital.utils.JwtUtil;
import com.squareup.digital.validators.AuthValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthService {

  private final AuthRepository authRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthValidator authValidator;
  private final JwtUtil jwtUtil;

  public AuthService(
      AuthRepository authRepository,
      PasswordEncoder passwordEncoder,
      AuthValidator authValidator,
      JwtUtil jwtUtil) {
    this.authRepository = authRepository;
    this.passwordEncoder = passwordEncoder;
    this.authValidator = authValidator;
    this.jwtUtil = jwtUtil;
  }

  @Transactional
  public String registerUser(UserModel user) {

    if (authRepository.existsByUsername(user.getUsername())) {
      throw new IllegalArgumentException("User already exists");
    }

    if (authRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new IllegalArgumentException("Email already in use");
    }

    authValidator.validateUser(user);

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setFollowers(0L);
    user.setFollowing(0L);
    user.setPosts(0L);
    user.setVerified(false);
    user.setCreatedAt(Instant.now());
    user.setUpdatedAt(Instant.now());
    authRepository.save(user);

    return "User registered successfully";
  }

  public LoginResponse loginUser(LoginRequest loginRequest) {
    UserModel user =
        authRepository
            .findByUsername(loginRequest.getUsername())
            .or(() -> authRepository.findByEmail(loginRequest.getEmail()))
            .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Invalid username or password");
    }
    String token =
        jwtUtil.generateToken(
            new LoginRequest(user.getUsername(), null, loginRequest.getPassword()));

    return new LoginResponse(token, user);
  }

  public ProfileResponse getProfile(String username) {
    UserModel user = authRepository.findByUsername(username).orElse(null);
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }
    ProfileResponse profileResponse = new ProfileResponse();
    profileResponse.setUsername(user.getUsername());
    profileResponse.setEmail(user.getEmail());
    profileResponse.setFirstName(user.getFirstName());
    profileResponse.setLastName(user.getLastName());
    profileResponse.setCreatedAt(user.getCreatedAt());
    profileResponse.setUpdatedAt(user.getUpdatedAt());
    profileResponse.setFollowers(user.getFollowers());
    profileResponse.setFollowing(user.getFollowing());
    profileResponse.setPosts(user.getPosts());
    return profileResponse;
  }
}
