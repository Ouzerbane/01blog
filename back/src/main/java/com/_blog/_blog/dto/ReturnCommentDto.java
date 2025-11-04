package com._blog._blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnCommentDto {
    private Long id;
    private String content;
    private Long user;
    private String craetAt;
    private Long count;
}
