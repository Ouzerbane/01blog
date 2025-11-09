package com._blog._blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.CommentsDto;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.ResponsCommetDto;
import com._blog._blog.dto.ReturnCommentDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.CommentsEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.CommentsRepo;
import com._blog._blog.model.repository.PostsRepo;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepo commentsRepo;

    @Autowired
    private PostsRepo postsRepo;

    public ResponsCommetDto addComment(CommentsDto commentsDto, AuthEntity authEntity) {
        PostsEntity post = postsRepo.findById(commentsDto.getId())
                .orElseThrow(() -> new CustomException("post", "Post not found"));

        CommentsEntity savedComment = commentsRepo.save(
                CommentsEntity.builder()
                        .content(commentsDto.getContent())
                        .user(authEntity)
                        .post(post)
                        .build()
        );

        // long count = commentsRepo.countByPostId(post.getId());
        return  new ResponsCommetDto(savedComment.getId(),commentsDto.getId(),authEntity.getId(),authEntity.getUsername(),savedComment.getContent(),savedComment.getCreatedAt());

        // return ReturnCommentDto.builder()
        //         .id(savedComment.getId())
        //         .user(authEntity.getId())
        //         .content(savedComment.getContent())
        //         .craetAt(savedComment.getCreatedAt().toString())
        //         .count(count)
        //         .build();
    }

     public List<ResponsCommetDto> getComment(IdDto postId) {
         return commentsRepo.findAllByPostIdOrderByCreatedAtAsc(postId.getId())
                .stream()
                .map(comment -> new ResponsCommetDto(
                        comment.getId(),
                        comment.getPost().getId(),
                        comment.getUser().getId(),
                        comment.getUser().getUsername(),
                        comment.getContent(),
                        comment.getCreatedAt()
                ))
                .collect(Collectors.toList());
    
    }
}
