package com.squareup.digital

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(private val jwtFilter: JwtFilter, private val userRepository: UserRepository) {
  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder(13)
  }

  @Bean
  @Throws(Exception::class)
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
        // 2. Enable CORS at the security level
        .cors {}
        .csrf { it.disable() }
        .authorizeHttpRequests {
          it.requestMatchers(
                  "/api/auth/register",
                  "/api/auth/login",
                  "/oauth2/**",
                  "/login/oauth2/**",
                  "/actuator/**",
                  "/api/authgoogle/success",
              )
              .permitAll()
              .anyRequest()
              .authenticated()
        }
        // 3. Prevent 302 Redirects for unauthenticated API requests
        .exceptionHandling {
          it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        }
        // Optional: If you aren't using Spring's default login page, remove this line entirely
        // .formLogin { Customizer.withDefaults<HttpSecurity>() }

        .oauth2Login { it.defaultSuccessUrl("http://localhost:5173/?oauth=google", true) }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) }
        .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

    return http.build()
  }

  @Bean
  @Throws(Exception::class)
  fun authenticationManager(): AuthenticationManager {
    return AuthenticationManager {
      val username = it.name
      val user =
          userRepository.findByUsername(username)
              ?: throw UsernameNotFoundException("User not found: $username")
      UsernamePasswordAuthenticationToken(user, null, emptyList())
    }
  }
}
