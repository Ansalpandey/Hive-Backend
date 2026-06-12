package com.squareup.digital.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final String secretKeyString =
            "YourSecretKjhkldasfhjkladsfljkdasfjklsadfhjkdsahfljksdahfljkasdhfjlkhsfjklsadhlfjkasdfasdfajdsklaf;jklfdjfkl;jfklnm,cxvnm,zxncv.uiefhuhjdksm,cvn.m,zxcvujifhewoiufhoeijfioejfiewsdey";

    private final SecretKey secretKey =
            Keys.hmacShaKeyFor(secretKeyString.getBytes());

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
}

