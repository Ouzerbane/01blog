package com._blog._blog.dto;

import java.util.UUID;

import com._blog._blog.model.entity.AuthEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAdminDto {
    private UUID id;
    private String username;
    private String imageUrl;
    private String action;

    public UserAdminDto(AuthEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.imageUrl = user.getImageUrl();
        this.action = user.getAction();
    }
}
