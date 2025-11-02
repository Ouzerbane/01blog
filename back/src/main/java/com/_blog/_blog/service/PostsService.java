package com._blog._blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.PostsDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.PostsRepo;
import com._blog._blog.util.jwt.JwtUtil;

import jakarta.validation.Valid;

@Service
public class PostsService {

    @Autowired
    private PostsRepo postsRepo;

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private JwtUtil jwtUtil;

    public PostsEntity savePost(PostsDto postsDto, String jwtToken) {

        Long userId = jwtUtil.getUserIdFromToken(jwtToken);

        Object author = authRepo.findById(userId)
                .orElseThrow(() -> new CustomException("user", "User not found"));

        PostsEntity post = postsDto.toEntity();
        post.setAuthor((AuthEntity) author);

        return postsRepo.save(post);
    }

    public PostsEntity editPost(PostsDto postsDto, String jwtToken) {
        Long userId = jwtUtil.getUserIdFromToken(jwtToken);
        Long postId = postsDto.getId(); 
        PostsEntity post = postsRepo.findById(postId)
                .orElseThrow(() -> new CustomException("post", "Post not found"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new CustomException("authorization", "You are not the author of this post");
        }

        post.setTitle(postsDto.getTitle());
        post.setContent(postsDto.getContent());
        post.setImageUrl(postsDto.getImageUrl());

        return postsRepo.save(post);
    }

     public void  deletePost(@Valid IdDto idDto, String jwtToken) {
        Long userId = jwtUtil.getUserIdFromToken(jwtToken);
        Long postId = idDto.getId(); 
        PostsEntity post = postsRepo.findById(postId)
                .orElseThrow(() -> new CustomException("post", "Post not found"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new CustomException("authorization", "You are not the author of this post");
        }

        

        postsRepo.delete(post);
    }

}

// public PostsEntity updatePost(Long postId, PostsDto postsDto, String jwtToken) {
//         Long userId = jwtUtil.getUserIdFromToken(jwtToken);
//         AuthEntity author = authRepo.findById(userId)
//                 .orElseThrow(() -> new CustomException("user", "User not found"));
//         PostsEntity post = postsRepo.findById(postId)
//                 .orElseThrow(() -> new CustomException("post", "Post not found"));
//         if (!post.getAuthor().getId().equals(author.getId())) {
//             throw new CustomException("authorization", "You are not allowed to update this post");
//         }
//         post.setTitle(postsDto.getTitle());
//         post.setContent(postsDto.getContent());
//         post.setImageUrl(postsDto.getImageUrl());
//         return postsRepo.save(post);
//     }
//     public void deletePost(Long postId, String jwtToken) {
//         Long userId = jwtUtil.getUserIdFromToken(jwtToken);
//         AuthEntity author = authRepo.findById(userId)
//                 .orElseThrow(() -> new CustomException("user", "User not found"));
//         PostsEntity post = postsRepo.findById(postId)
//                 .orElseThrow(() -> new CustomException("post", "Post not found"));
//         if (!post.getAuthor().getId().equals(author.getId())) {
//             throw new CustomException("authorization", "You are not allowed to delete this post");
//         }
//         postsRepo.delete(post);
//     }
//     public List<PostsEntity> getPostsByAuthors(List<AuthEntity> authors) {
//         return postsRepo.findAll().stream()
//                 .filter(p -> authors.contains(p.getAuthor()))
//                 .toList();
//     }
