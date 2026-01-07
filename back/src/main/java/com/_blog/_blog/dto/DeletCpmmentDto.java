package com._blog._blog.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DeletCpmmentDto {
    private UUID id;
    private UUID postId;
}
