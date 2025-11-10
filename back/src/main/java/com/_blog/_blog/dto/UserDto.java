package com._blog._blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String username ;
    private String email ;
    private String imagUrl ;
    private Long followers ;
    private Long following ;
}
