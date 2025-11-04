package com._blog._blog.dto;

import lombok.Data;

@Data
public class ReportDto {
    private Long targetUserId;
    private String reason;
}
