package com._blog._blog.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDto {
    private UUID id;
    @NotBlank(message = "Content is required")
    @Size(max = 500, min = 1, message = "Content must not exceed 500 characters")
    private String content;
}
