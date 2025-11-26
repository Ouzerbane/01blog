
package com._blog._blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.LoginRequest;
import com._blog._blog.dto.RegisterRequest;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.JwtEntity;
import com._blog._blog.model.repository.JawtRepo;
import com._blog._blog.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

// @AllArgsConstructor
// @NoArgsConstructor

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class Auth {
    @Autowired
    private AuthService emptyService;

    @Autowired
    private JawtRepo jawtRepo;

    @PostMapping("/regester")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        AuthEntity savedUser = emptyService.registerservece(req.toAuthEntity());

        return ResponseEntity.ok(new ApiResponse<>(true, null, List.of()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        String token = emptyService.login(request.getUsernameOrEmail(), request.getPassword());

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                // .sameSite("None")
                .secure(false)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(HttpServletRequest request, HttpServletResponse response) {

        String token = (String) request.getAttribute("jwt");

        if (token != null) {
            jawtRepo.save(JwtEntity.builder().jwt(token).build());
        }

        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", deleteCookie.toString());

        return ResponseEntity.ok(new ApiResponse<>(true, null, null));
    }

}
