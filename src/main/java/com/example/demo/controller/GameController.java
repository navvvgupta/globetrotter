package com.example.demo.controller;

import com.example.demo.dto.AnswerResponseDto;
import com.example.demo.dto.ChallengeResponseDto;
import com.example.demo.dto.GameHistoryDto;
import com.example.demo.dto.QuestionResponseDto;
import com.example.demo.entity.GameSession;
import com.example.demo.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameSessionService gameSessionService;

    @PostMapping("/start")
    public ResponseEntity<Long> startGame(@RequestParam Long userId,
                                          @RequestParam int totalQuestions,
                                          @RequestParam int timePerQuestion) {
        return ResponseEntity.ok(gameSessionService.startGame(userId, totalQuestions, timePerQuestion));
    }

    @GetMapping("/question/next")
    public ResponseEntity<QuestionResponseDto> getNextQuestion() {
        return ResponseEntity.ok(gameSessionService.getQuestionWithOptions());
    }

    @GetMapping("/question/clue")
    public ResponseEntity<String> getClue(@RequestParam Long questionId, @RequestParam int clueLevel,@RequestParam Long sessionId) {
        return ResponseEntity.ok(gameSessionService.getClue(questionId, clueLevel,sessionId));
    }

    @PostMapping("/question/answer")
    public ResponseEntity<AnswerResponseDto> submitAnswer(@RequestParam Long sessionId,
                                                          @RequestParam Long questionId,
                                                          @RequestParam String selectedCity,
                                                          @RequestParam int timeTaken) {
        AnswerResponseDto result = gameSessionService.evaluateAnswer(sessionId, questionId, selectedCity, timeTaken);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/end/{sessionId}")
    public ResponseEntity<String> endGame(@PathVariable Long sessionId) {
        gameSessionService.endGame(sessionId);
        return ResponseEntity.ok("Game ended and score saved.");
    }

    @PostMapping("/challenge")
    public ResponseEntity<String> createChallenge(@RequestParam String inviterUsername,
                                                  @RequestParam String inviteeUsername,
                                                  @RequestParam int totalQuestions,
                                                  @RequestParam int timePerQuestion) {
        return ResponseEntity.ok(
                gameSessionService.createChallenge(inviterUsername, inviteeUsername, totalQuestions, timePerQuestion));
    }

    @PostMapping("/challenge/accept")
    public ResponseEntity<ChallengeResponseDto> acceptChallenge(@RequestParam String challengeCode) {
            return ResponseEntity.ok(gameSessionService.acceptChallenge(challengeCode));
    }

    // In GameController.java

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<GameHistoryDto>> getGameHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(gameSessionService.getRecentGames(userId, 5));
    }
}

