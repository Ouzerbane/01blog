package com._blog._blog.util.jwt;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // --- generate token with id + username
    public String generateToken(UUID userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId.toString())
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(key)
                .compact();
    }

    // --- extract token from cookies
    public static String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // --- extract claims from token
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // --- get username
    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    // --- get userId as UUID
    public UUID getUserIdFromToken(String token) {
        Object userId = extractClaims(token).get("userId");
        if (userId == null)
            return null;
        return UUID.fromString(userId.toString());
    }

    // --- check token validity
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- check if token is still valid within 24 hours
    public boolean isTokenNotExpired24h(String token) {
        try {
            Claims claims = extractClaims(token);

            Date issuedAt = (Date) claims.getIssuedAt();

            if (issuedAt == null)
                return false;

            Date now = new Date();

            Date expiry24h = new Date(issuedAt.getTime() + 24 * 60 * 60 * 1000);

            return now.before(expiry24h);
        } catch (Exception e) {
            return false;
        }
    }

}
