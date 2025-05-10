package com.example.demo.dto;

import com.example.demo.entity.GameSession;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GameHistoryDto {
    private Long sessionId;
    private int score;
    private int correctAnswers;
    private int incorrectAnswers;
    private LocalDateTime playedAt;

    public static GameHistoryDto fromEntity(GameSession session) {
        return GameHistoryDto.builder()
                .sessionId(session.getId())
                .score(session.getScoreThisGame())
                .correctAnswers(session.getCorrectCount())
                .incorrectAnswers(session.getIncorrectCount())
                .playedAt(session.getPlayedAt())
                .build();
    }
}
