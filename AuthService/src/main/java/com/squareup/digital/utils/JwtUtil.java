package com.squareup.digital.utils;

import com.squareup.digital.dto.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final String secretKeyString =
      "YourSecretKjhkldasfhjkladsfljkdasfjklsadfhjkdsahfljksdahfljkasdhfjlkhsfjklsadhlfjkasdfasdfajdsklaf;jklfdjfkl;jfklnm,cxvnm,zxncv.uiefhuhjdksm,cvn.m,zxcvujifhewoiufhoeijfioejfiewsdey";

  private final SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());

  public String generateToken(LoginRequest loginRequest) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", loginRequest.getUsername());
    claims.put("email", loginRequest.getEmail());

    long jwtExpirationMs = 86400000;
    return Jwts.builder()
        .claims(claims)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parse(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String extractUsername(String token) {
    return (String)
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("username");
  }

  public String extractEmail(String token) {
    return (String)
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("email");
  }
}
