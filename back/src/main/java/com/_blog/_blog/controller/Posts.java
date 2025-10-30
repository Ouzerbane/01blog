package com._blog._blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.PostsDto;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.service.PostsService;
// import com._blog._blog.service.PostsServece;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
public class Posts {
    @Autowired
    private PostsService emptyService;

    @PostMapping("/add-post")
    public ResponseEntity<ApiResponse<?>> addPost(@RequestBody PostsDto dto, @CookieValue(name = "jwt") String jwt) {

        PostsEntity saved = emptyService.savePost(dto, jwt);
        return ResponseEntity.ok(new ApiResponse<Object>(true, null, null));
    }

}
