package com._blog._blog.util.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.ErrorItem;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.repository.AuthRepo;
import static com._blog._blog.util.jwt.JwtUtil.extractTokenFromCookie;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthRepo authRepo;

    public JwtAuthFilter(JwtUtil jwtUtil, AuthRepo authRepo) {
        this.jwtUtil = jwtUtil;
        this.authRepo = authRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractTokenFromCookie(request);
        String path = request.getRequestURI();

        // boolean checkValidationJwt = jwtUtil.isTokenValid(token) && token != null;
        if (path.startsWith("/regester") || path.startsWith("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token == null || !jwtUtil.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            ApiResponse<Object> errorResponse;
            errorResponse = new ApiResponse<>(
                    false,
                    List.of(new ErrorItem("token", "Invalid or missing token")),
                    null);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(errorResponse);

            response.getWriter().write(json);
            return;
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        AuthEntity user = authRepo.findById(userId).orElse(null);
        if (!user.getType().equals("ADMIN")) {
            if (user.getAction().equals("BAN")) {
                ApiResponse<Object> errorResponse;
                errorResponse = new ApiResponse<>(
                        false,
                        List.of(new ErrorItem("user", "User is Ban")),
                        null);

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(errorResponse);
                response.getWriter().write(json);
                return;
            }
        }

        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
                    new ArrayList<>());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // System.out.println("====> Token valid for user id: " + userId);
        filterChain.doFilter(request, response);
    }

}
