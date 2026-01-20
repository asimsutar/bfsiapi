package com.tcs.util;

import java.security.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component  
public class JwtUtil {

    // SAME secret as auth-service & gateway
    private static final String SECRET_KEY = "mySuperSecretKeyForJwtGeneration123456";

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public String extractKycStatus(String token) {
        return getClaims(token).get("kyc", String.class);
    }
}
