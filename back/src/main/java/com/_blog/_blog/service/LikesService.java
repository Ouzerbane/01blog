package com._blog._blog.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.LikeDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.LikesEntity;
import com._blog._blog.model.entity.NotificationEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.LikesRepo;
import com._blog._blog.model.repository.NotificationRepo;
import com._blog._blog.model.repository.PostsRepo;
import com._blog._blog.util.NotificationType;

import jakarta.transaction.Transactional;

@Service
public class LikesService {

    @Autowired
    private LikesRepo likesRepo;

    @Autowired
    private PostsRepo postsRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    @Transactional
    public LikeDto toggleLike(IdDto postIdDto, AuthEntity currentUser) {
        PostsEntity post = postsRepo.findById(postIdDto.getId())
                .orElseThrow(() -> new CustomException("post", "Post not found"));

        Optional<LikesEntity> like = likesRepo.findByUserIdAndPostId(currentUser.getId(), post.getId());
        if (like.isPresent()) {
            likesRepo.delete(like.get());
            if (notificationRepo.existsBySenderIdAndUserIdAndTypeAndPostId(currentUser.getId(), post.getAuthor().getId(), NotificationType.LIKE, post.getId())) {
                notificationRepo.deleteBySenderIdAndUserIdAndTypeAndPostId(
                        currentUser.getId(),
                        post.getAuthor().getId(),
                        NotificationType.LIKE,
                        post.getId()
                );
            }
            return countLikes(post.getId(), currentUser.getId());
        }
        likesRepo.save(LikesEntity.builder().user(currentUser).post(post).build());
        if (!Objects.equals(post.getAuthor().getId(), currentUser.getId())) {
            NotificationEntity notification = NotificationEntity.builder()
                    .message(currentUser.getUsername() + " Like your post")
                    .user(post.getAuthor())
                    .sender(currentUser)
                    .read(false)
                    .type(NotificationType.LIKE)
                    .postId(post.getId())
                    .build();
            notificationRepo.save(notification);
        }

        return countLikes(post.getId(), currentUser.getId());

    }

    public LikeDto countLikes(Long postId, Long userid) {
        return LikeDto.builder().count(likesRepo.countByPostId(postId))
                .like(likesRepo.existsByUserIdAndPostId(userid, postId)).build();

    }
}
