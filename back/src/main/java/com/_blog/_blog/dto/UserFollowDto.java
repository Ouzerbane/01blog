package com._blog._blog.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollowDto {
    private UUID id;
    private String username;
    private String imageUrl ; 
    private boolean followed;
}
