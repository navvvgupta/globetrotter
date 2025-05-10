// entity/GameSession.java
package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "game_sessions")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int scoreThisGame;

    private int correctCount;

    private int incorrectCount;

    private LocalDateTime playedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
