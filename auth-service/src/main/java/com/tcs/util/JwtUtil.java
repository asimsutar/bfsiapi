package com.tcs.util;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {
	private static final String SECRET =
            "mySuperSecretKeyForJwtGeneration123456";

    private static final long EXPIRATION = 1000 * 60 * 60; 

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username ,String role, String kycStatus) {

        return Jwts.builder()
        		.setSubject(username)
                .claim("role", role)
                .claim("kyc", kycStatus)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
