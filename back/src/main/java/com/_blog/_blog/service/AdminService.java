package com._blog._blog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._blog._blog.dto.IdDto;
import com._blog._blog.dto.PostAdminDto;
import com._blog._blog.dto.PostMediaDto;
import com._blog._blog.dto.ReportDataDto;
import com._blog._blog.dto.UserAdminDto;
import com._blog._blog.exception.CustomException;
import com._blog._blog.model.entity.AuthEntity;
import com._blog._blog.model.entity.PostsEntity;
import com._blog._blog.model.entity.ReportEntity;
import com._blog._blog.model.repository.AuthRepo;
import com._blog._blog.model.repository.PostsRepo;
import com._blog._blog.model.repository.ReportRepo;

@Service
public class AdminService {

    @Autowired
    private AuthRepo authRepo;

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private PostsRepo postRepo;

    public String removeUserService(IdDto id, AuthEntity currentUser) {
        if (!"ADMIN".equals(currentUser.getType())) {
            throw new CustomException("ACCESS_DENIED", "You are not allowed to access statistics");
        }
        AuthEntity user = authRepo.findById(id.getId())
                .orElseThrow(() -> new CustomException("user", "User not found"));
        ;
        authRepo.delete(user);
        return "User with ID " + id.getId() + " has been removed successfully.";
    }

    public String removeReportService(IdDto id, AuthEntity currentUser) {
        if (!"ADMIN".equals(currentUser.getType())) {
            throw new CustomException("ACCESS_DENIED", "You are not allowed to access statistics");
        }
        ReportEntity user = reportRepo.findById(id.getId())
                .orElseThrow(() -> new CustomException("post", "post not found"));
        ;
        reportRepo.delete(user);
        return "post has been removed successfully.";
    }

    public List<ReportDataDto> getReportService(AuthEntity currentUser) {
        if (!"ADMIN".equals(currentUser.getType())) {
            throw new CustomException("ACCESS_DENIED", "You are not allowed to access statistics");
        }
        List<ReportDataDto> reports = reportRepo.findAllOrderByTime().stream()
                .map(repo ->{
                    ReportDataDto dto = new ReportDataDto();
                    dto.setId(repo.getId());
                    dto.setReason(repo.getReason());
                    dto.setReporter(repo.getReporter().getUsername());
                    dto.setReporterId(repo.getReporter().getId());

                    if (repo.getTargetUser() != null) {
                        dto.setTargetUser(repo.getTargetUser().getUsername());
                        dto.setTargetUserId(repo.getTargetUser().getId());
                        dto.setType("USER");
                    }
                    if (repo.getTargetPost() != null) {
                        dto.setTargetPost(repo.getTargetPost().getTitle());
                        dto.setTargetPostId(repo.getTargetPost().getId());
                        dto.setType("POST");
                    }
                    return dto; 
                })
                .collect(Collectors.toList());

        return reports;
    }

    public String BanService(AuthEntity currentUser, IdDto id) {
        if (!"ADMIN".equals(currentUser.getType())) {
            throw new CustomException("ACCESS_DENIED", "You are not allowed to access statistics");
        }
        AuthEntity banUser = authRepo.findById(id.getId())
                .orElseThrow(() -> new CustomException("user", "User not found"));
        if (banUser.getAction().equals("BAN")) {
            banUser.setAction("ACTIVE");
        } else {
            banUser.setAction("BAN");
        }
        authRepo.save(banUser);
        return "successfully";
    }

    public String ChangeStatusService(AuthEntity currentUser, IdDto id) {
        if (!"ADMIN".equals(currentUser.getType())) {
            throw new CustomException("ACCESS_DENIED", "You are not allowed to access statistics");
        }
        PostsEntity post = postRepo.findById(id.getId())
                .orElseThrow(() -> new CustomException("post", "post not found"));
        if (post.getStatus().equals("show")) {
            post.setStatus("Hide");
        } else {
            post.setStatus("show");
        }
        postRepo.save(post);
        return "successfully";
    }

    public List<UserAdminDto> getUsersService(AuthEntity currentUser) {
        if (!"ADMIN".equals(currentUser.getType())) {
            throw new CustomException("ACCESS_DENIED", "You are not allowed to access statistics");
        }
        List<AuthEntity> users = authRepo.findAllExcept(currentUser.getId());

        List<UserAdminDto> dtos = users.stream()
                .map(user -> new UserAdminDto(user))
                .collect(Collectors.toList());
        return dtos;
    }

    public List<PostAdminDto> getPostsService(AuthEntity currentUser) {
        if (!"ADMIN".equals(currentUser.getType())) {
            throw new CustomException("ACCESS_DENIED", "You are not allowed to access statistics");
        }

        List<PostsEntity> Posts = postRepo.findAllByOrderByCreatedAtDesc();
        List<PostAdminDto> postsDto = Posts.stream()
                .map(post -> {
                    List<PostMediaDto> mediaDtos = post.getMedia().stream()
                            .map(media -> PostMediaDto.builder()
                                    .id(media.getId())
                                    .url(media.getMediaUrl())
                                    .type(media.getMediaType().name())
                                    .build())
                            .collect(Collectors.toList());
                    return PostAdminDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            // .imageUrl(post.getImageUrl())
                            .media(mediaDtos)
                            .createdAt(post.getCreatedAt())
                            .status(post.getStatus())
                            .author(PostAdminDto.AuthorDto.builder()
                                    .id(post.getAuthor().getId())
                                    .username(post.getAuthor().getUsername())
                                    .email(post.getAuthor().getEmail())
                                    .imageUrl(post.getAuthor().getImageUrl())
                                    .build())
                            .build();
                })
                .collect(Collectors.toList());
        return postsDto;
    }

}

// if (!"admin".equalsIgnoreCase(currentUser.getType())) {
// throw new CustomException("authorization", "You are not authorized to remove
// users");
// }
