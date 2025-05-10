// dto/UserResponseDto.java
package com.example.demo.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private int totalScore;
}
