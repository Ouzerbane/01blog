package com._blog._blog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.IdDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.LikesEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.repository.LikesRepo;
import com._blog._blog.model.repository.PostsRepo;

@Service
public class LikesService {
    @Autowired
    private LikesRepo likesRepo;

    @Autowired
    private PostsRepo postsRepo;

    public Long toggleLike(IdDto postIdDto, AuthEntity currentUser) {
        PostsEntity post = postsRepo.findById(postIdDto.getId()).orElseThrow(() -> new CustomException("post", "Post not found"));
      
           Optional<LikesEntity> like = likesRepo.findByUserIdAndPostId(currentUser.getId(), post.getId());
           if (like.isPresent()){
            likesRepo.delete(like.get());
            return countLikes(post.getId());
           }
            likesRepo.save(LikesEntity.builder().user(currentUser).post(post).build());
            return countLikes(post.getId());
        

    }

       public Long countLikes(Long postId) {
        return likesRepo.countByPostId(postId);
    }
}
