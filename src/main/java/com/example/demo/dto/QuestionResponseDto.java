package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponseDto {
    private Long questionId;
    private int clueLevel;
    private int totalClues;
    private String clue;
    private List<String> options;  // Added this
}
