package com._blog._blog.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.CommentsDto;
import com._blog._blog.dto.DeletCpmmentDto;
import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.ResponsCommetDto;
import com._blog._blog.exception.ForbiddenException;
import com._blog._blog.exception.NotFoundException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.CommentsEntity;
import com._blog._blog.model.entity.NotificationEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.CommentsRepo;
import com._blog._blog.model.repository.NotificationRepo;
import com._blog._blog.model.repository.PostsRepo;
import com._blog._blog.util.NotificationType;

@Service
public class CommentsService {

        @Autowired
        private CommentsRepo commentsRepo;

        @Autowired
        private PostsRepo postsRepo;

        @Autowired
        private NotificationRepo notificationRepo;

        public ResponsCommetDto addComment(CommentsDto commentsDto, AuthEntity authEntity) {
                PostsEntity post = postsRepo.findById(commentsDto.getId())
                                .orElseThrow(() -> new NotFoundException("post", "Post not found"));

                CommentsEntity savedComment = commentsRepo.save(
                                CommentsEntity.builder()
                                                .content(commentsDto.getContent())
                                                .user(authEntity)
                                                .post(post)
                                                .build());
                if (!Objects.equals(authEntity.getId(), post.getAuthor().getId())) {

                        NotificationEntity notification = NotificationEntity.builder()
                                        .message(authEntity.getUsername() + "add comment to post "
                                                        + post.getTitle())
                                        .user(post.getAuthor())
                                        .read(false)
                                        .postId(post.getId())
                                        .type(NotificationType.COMMENT)
                                        .build();
                        notificationRepo.save(notification);
                }

                return new ResponsCommetDto(savedComment.getId(), commentsDto.getId(), authEntity.getId(),
                                authEntity.getUsername(), savedComment.getContent(), authEntity.getImageUrl(), savedComment.getCreatedAt(), true);

        }

        public List<ResponsCommetDto> getComment(IdDto postId , AuthEntity currentUser) {
                return commentsRepo.findAllByPostIdOrderByCreatedAtDesc(postId.getId())
                                .stream()
                                .map(comment -> {
                                        boolean canDelete = comment.getUser().getId().equals(currentUser.getId());
                                        return new ResponsCommetDto(
                                                        comment.getId(),
                                                        comment.getPost().getId(),
                                                        comment.getUser().getId(),
                                                        comment.getUser().getUsername(),
                                                        comment.getContent(),
                                                        comment.getUser().getImageUrl(),
                                                        comment.getCreatedAt(),
                                                        canDelete);
                                        })
                                .collect(Collectors.toList());
        }

        public Long deleteComment(DeletCpmmentDto commentId, AuthEntity currentUser) {
                CommentsEntity comment = commentsRepo.findById(commentId.getId())
                                .orElseThrow(() -> new NotFoundException("comment", "Comment not found"));

                PostsEntity post = postsRepo.findById(commentId.getPostId())
                                .orElseThrow(() -> new NotFoundException("post", "Post not found"));

                if (!comment.getUser().getId().equals(currentUser.getId())) {
                        throw new ForbiddenException("comment", "You are not authorized to delete this comment");
                }
                commentsRepo.delete(comment);
                Long count = commentsRepo.countByPostId(post.getId());
                return count;
        }
}
