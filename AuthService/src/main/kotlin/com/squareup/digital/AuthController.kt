package com.squareup.digital

import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(private val userService: UserService) {

  @GetMapping
  fun index(): String {
    return "Welcome to the Auth API!"
  }

  // 1. Standard Registration
  @PostMapping("/register")
  fun register(@Valid @RequestBody request: UserModel): String {
    userService.registerUser(request)
    return "User registered successfully"
  }

  // 2. Standard Login
  @PostMapping("/login")
  fun login(@Valid @RequestBody request: LoginRequest): LoginResponse {
    return userService.loginUser(request)
  }

  // 3. Google OAuth2 Login
  @GetMapping("/google/success")
  fun googleLogin(@AuthenticationPrincipal principal: OAuth2User): LoginResponse {
    return userService.processGoogleLogin(principal)
  }
}
