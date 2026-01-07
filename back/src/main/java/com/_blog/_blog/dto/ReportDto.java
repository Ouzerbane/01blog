package com._blog._blog.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ReportDto {
    private UUID targetUserId;
    private String reason;
    private UUID targetPostId;
}
