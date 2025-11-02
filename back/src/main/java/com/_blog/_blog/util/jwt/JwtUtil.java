package com._blog._blog.util.jwt;

import java.security.Key;

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
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .signWith(key)
                .compact();
    }

 

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

    // --- extract claims mn token
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // --- tjbd username
    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    // --- tjbd userId
    public Long getUserIdFromToken(String token) {
        Object userId = extractClaims(token).get("userId");

        if (userId == null) {
            return null;
        }

        // return (Long) userId ;
        return Long.valueOf(userId.toString());
    }


    public boolean isTokenValid(String token) {
    try {
        extractClaims(token);
        return true;
    } catch (Exception e) {
        return false;
    }
}


}
