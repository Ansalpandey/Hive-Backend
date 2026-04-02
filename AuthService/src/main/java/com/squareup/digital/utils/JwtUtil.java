package com.squareup.digital.utils;

import com.squareup.digital.dto.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String secretKeyString =
            "YourSecretKjhkldasfhjkladsfljkdasfjklsadfhjkdsahfljksdahfljkasdhfjlkhsfjklsadhlfjkasdfasdfajdsklaf;jklfdjfkl;jfklnm,cxvnm,zxncv.uiefhuhjdksm,cvn.m,zxcvujifhewoiufhoeijfioejfiewsdey";

    private final SecretKey secretKey =
            Keys.hmacShaKeyFor(secretKeyString.getBytes());

    public String generateToken(LoginRequest loginRequest) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", loginRequest.getUsername());

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
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return (String) Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username");
    }
}
