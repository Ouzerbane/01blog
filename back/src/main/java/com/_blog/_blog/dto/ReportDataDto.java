package com._blog._blog.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDataDto {
    private UUID id;
    private String reason;

    private String reporter;
    private UUID reporterId ;

    private String targetUser;
    private UUID targetUserId ;

    private String type;

    private String targetPost;
    private UUID targetPostId;

    private LocalDateTime time;

}
