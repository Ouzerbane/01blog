package com._blog._blog.dto;

import org.springframework.web.multipart.MultipartFile;

import com._blog._blog.exception.CustomException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostRequest {
    private String title;
    private String content;
    private MultipartFile image;

    public void cheData() {
        content = content.trim();
        title = title.trim();
        if (content.length() == 0 || title.length() == 0) {
            throw new CustomException(
                    "title",
                    "It should not contain just space");

        }
    }
}
