// dto/GameSessionResponseDto.java
package com.example.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameSessionResponseDto {
    private Long id;
    private int score;
    private String summary;
    private LocalDateTime playedAt;
    private Long userId;
}
