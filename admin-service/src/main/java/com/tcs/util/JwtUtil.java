package com.tcs.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // MUST be same in all services
    private static final String SECRET_KEY =
            "mySuperSecretKeyForJwtGeneration123456";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24; // 24 hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /* ================= TOKEN GENERATION ================= */

    public String generateToken(String username, String role, String kycStatus) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("kyc", kycStatus)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())   
                .compact();
    }

    /* ================= TOKEN PARSING ================= */

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())   // ✅ FIXED
                .build()
                .parseSignedClaims(cleanToken(token))
                .getPayload();
    }

    private String cleanToken(String token) {
        return token.startsWith("Bearer ")
                ? token.substring(7)
                : token;
    }

    /* ================= EXTRACTORS ================= */

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    public String extractKycStatus(String token) {
        return getClaims(token).get("kyc", String.class);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
