package com._blog._blog.util.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.ErrorItem;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.JawtRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com._blog._blog.util.jwt.JwtUtil.extractTokenFromCookie;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthRepo authRepo;

    @Autowired
    private JawtRepo jwtRepo;

    public JwtAuthFilter(JwtUtil jwtUtil, AuthRepo authRepo) {
        this.jwtUtil = jwtUtil;
        this.authRepo = authRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        // السماح بالوصول لمسارات التسجيل والدخول بدون JWT
        if (path.startsWith("/regester") || path.startsWith("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractTokenFromCookie(request);

        if (token == null || !jwtUtil.isTokenValid(token) || jwtRepo.existsByJwt(token)) {
            sendError(response, "token", "Invalid or missing token", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UUID userId = jwtUtil.getUserIdFromToken(token);
        AuthEntity user = authRepo.findById(userId).orElse(null);

        if (user == null) {
            sendError(response, "user", "User not found", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (!"ADMIN".equals(user.getType()) && "BAN".equals(user.getAction())) {
            sendError(response, "user", "User is banned", HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            request.setAttribute("jwt", token);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, String field, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        ApiResponse<Object> errorResponse =
                new ApiResponse<>(false, List.of(new ErrorItem(field, message)), null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), errorResponse);
    }
}
