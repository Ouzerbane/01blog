package com._blog._blog.util.jwt;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String username) {
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
    }
}

