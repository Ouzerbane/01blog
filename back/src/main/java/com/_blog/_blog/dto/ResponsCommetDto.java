package com._blog._blog.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsCommetDto {
    private UUID id;
    private UUID postId;
    private UUID userId;
    private String username;
    private String content;
    private String url ;
    private LocalDateTime createdAt;
    private boolean candelet;
    // private Long count ;
}
