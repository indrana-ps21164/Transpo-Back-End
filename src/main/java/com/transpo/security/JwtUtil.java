package com.transpo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String jwtSecret;
    @Value("${jwt.expirationMs}") private long jwtExpirationMs;

    private Key getKey() { return Keys.hmacShaKeyFor(jwtSecret.getBytes()); }

    public String generateToken(String username, Long userId, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getKey())
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
    }

    public boolean validate(String token) {
        try { parse(token); return true; } catch (JwtException ex) { return false; }
    }
}
