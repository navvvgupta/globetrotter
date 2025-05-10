package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponseDto {
    private boolean correct;
    private int pointsEarned;
    private int totalScore;
    private int correctCount;
    private int incorrectCount;
    private List<String> funFacts;
}
