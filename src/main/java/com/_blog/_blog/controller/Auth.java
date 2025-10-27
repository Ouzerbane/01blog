

package com._blog._blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.LoginRequest;
import com._blog._blog.dto.RegisterRequest;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.service.EmptyService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

// @AllArgsConstructor
// @NoArgsConstructor

@RestController
public class Auth {
    @Autowired
    private EmptyService emptyService;


  @PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
    AuthEntity savedUser = emptyService.registerservece(req.toAuthEntity());

    return ResponseEntity.ok(new ApiResponse<>(true, null, List.of()));
}


   @PostMapping("/login")
public ResponseEntity<ApiResponse<Object>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
    String token = emptyService.login(request.getUsernameOrEmail(), request.getPassword());

    ResponseCookie cookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(24 * 60 * 60)
            .sameSite("Strict")
            .build();

    response.addHeader("Set-Cookie", cookie.toString());

    return ResponseEntity.ok(new ApiResponse<>(true, null, null));
}


    // @GetMapping("/delet")
    // public ResponseEntity<?> deletuser(@RequestParam int id) {
    //     emptyService.deletusres(id) ;
    //     return ResponseEntity.ok("ok");
    // }
}
