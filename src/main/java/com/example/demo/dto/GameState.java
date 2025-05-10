package com.example.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameState {
    private int totalScore;
    private int correctCount;
    private int incorrectCount;
    private int totalQuestions;
    private int clueTaken;
    private int timeLimitPerQuestion; // in seconds
    private LocalDateTime startedAt;
}
