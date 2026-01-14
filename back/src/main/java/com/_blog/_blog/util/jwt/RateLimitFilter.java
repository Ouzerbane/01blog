package com._blog._blog.util.jwt;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.ErrorItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter{

    private static final int MAX_REQUESTS = 100;
    private static final long WINDOW_TIME = 60_000; // 1 minute

    private final ConcurrentHashMap<String, RequestInfo> requests = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        long now = System.currentTimeMillis();

        RequestInfo info = requests.get(ip);

        if (info == null || now - info.startTime > WINDOW_TIME) {
            requests.put(ip, new RequestInfo(now, 1));
        } else {
            if (info.count >= MAX_REQUESTS) {
                sendError(response);
                return;
            }
            info.count++;
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<Object> error =
                new ApiResponse<>(false,
                        java.util.List.of(new ErrorItem("rate_limit", "Too many requests")),
                        null);

        new ObjectMapper().writeValue(response.getWriter(), error);
    }

    private static class RequestInfo {
        long startTime;
        int count;

        RequestInfo(long startTime, int count) {
            this.startTime = startTime;
            this.count = count;
        }
    }
    
}
