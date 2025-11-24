package com._blog._blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDataDto {
    private Long id;
    private String reporter;
    private String targetUser;
    private String reason;
}
