package com._blog._blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.PostsDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.PostsRepo;

@Service
public class PostsService {

    @Autowired
    private PostsRepo postsRepo;

    public PostsEntity savePost(PostsDto postsDto, AuthEntity currentUser) {

        PostsEntity post = postsDto.toEntity();
        post.setAuthor(currentUser);
        return postsRepo.save(post);
    }

    public PostsEntity editPost(PostsDto postsDto, AuthEntity currentUser) {
        PostsEntity post = postsRepo.findById(postsDto.getId())
                .orElseThrow(() -> new CustomException("post", "Post not found"));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException("authorization", "You are not the author of this post");
        }

        post.setTitle(postsDto.getTitle());
        post.setContent(postsDto.getContent());
        post.setImageUrl(postsDto.getImageUrl());
        return postsRepo.save(post);
    }

    public void deletePost(IdDto idDto, AuthEntity currentUser) {
        PostsEntity post = postsRepo.findById(idDto.getId())
                .orElseThrow(() -> new CustomException("post", "Post not found"));
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException("authorization", "You are not the author of this post");
        }
        postsRepo.delete(post);
    }

    public Page<PostsEntity> getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postsRepo.findAllByOrderByCreatedAtDesc(pageable);
    }

}
