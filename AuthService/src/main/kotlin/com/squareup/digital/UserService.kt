package com.squareup.digital

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepository: UserRepository,
        private val bcryptPasswordEncoder: PasswordEncoder,
        private val jwtUtil: JwtUtil,
        ) {
    fun registerUser(user: UserModel): String {
        val existingUser = userRepository.findByUsername(user.username)
        if (existingUser != null) {
            throw IllegalArgumentException("User already exists")
        }
        if (user.username.isBlank() || user.password.isBlank()) {
            throw IllegalArgumentException("Password and username cannot be empty")
        }
        if (user.password.length < 6) {
            throw IllegalArgumentException("Password must be at least 6 characters long")
        }
        if (user.username.length < 4) {
            throw IllegalArgumentException("Username must be at least 4 characters long")
        }
        if (user.username.length > 20) {
            throw IllegalArgumentException("Username must be at most 20 characters long")
        }
        if (user.password.length > 20) {
            throw IllegalArgumentException("Password must be at most 20 characters long")
        }
        if (!user.username.matches(Regex("^[a-zA-Z0-9]*$"))) {
            throw IllegalArgumentException("Username must contain only letters and numbers")
        }
        if (
                !user.password.matches(
                        Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*$")
                )
        ) {
            throw IllegalArgumentException(
                    "Password must contain at least one capital letter, one number, and one special character."
            )
        }
        user.password = bcryptPasswordEncoder.encode(user.password)

        userRepository.save(user)

        return "User registered successfully"
    }

    fun loginUser(loginRequest: LoginRequest): LoginResponse {
        val dbUser = userRepository.findByUsername(loginRequest.username)
        if (loginRequest.username.isEmpty()) {
            throw IllegalArgumentException("Username cannot be empty")
        }
        if (dbUser != null && bcryptPasswordEncoder.matches(loginRequest.password, dbUser.password)) {
            val token = jwtUtil.generateToken(loginRequest)
            return LoginResponse(token, dbUser)
        }
        throw IllegalArgumentException("Invalid username or password")
    }

    fun processGoogleLogin(principal: OAuth2User): LoginResponse {
        // 1. Safely extract the email (Google reliably returns this if the "email" scope is requested)
        val email =
                principal.getAttribute<String>("email")
                        ?: throw IllegalArgumentException("Google account does not have an associated email.")

        // 2. Extract standard Google profile attributes
        val firstName = principal.getAttribute<String>("given_name") ?: ""
        val lastName = principal.getAttribute<String>("family_name") ?: ""
        val profileImage = principal.getAttribute<String>("picture") ?: ""

        // 3. Find existing user OR create a new one using the Google Email
        val user =
                userRepository.findByUsername(email)
                        ?: userRepository.save(
                UserModel(
                        username = email,
                        email = email,
                        firstName = firstName,
                        lastName = lastName,
                        profile = profileImage,
                        password = "", // Placeholder since they authenticate via Google
                        projects = emptyList(),
                        )
        )

        // Store user to db if not exists

        userRepository.save(user)

        // 4. Generate your JWT
        val token = jwtUtil.generateToken(LoginRequest(username = user.username, password = ""))

        return LoginResponse(token, user)
    }
}
