// dto/GameSessionRequestDto.java
package com.example.demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameSessionRequestDto {
    private int score;
    private int correct;
    private int incorrect;
    private String summary;
}
