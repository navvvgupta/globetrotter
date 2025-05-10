package com.example.demo.service;

import com.example.demo.dto.AnswerResponseDto;
import com.example.demo.dto.QuestionResponseDto;
import com.example.demo.entity.ChallengeSession;
import com.example.demo.entity.DestinationDetails;
import com.example.demo.entity.GameSession;
import com.example.demo.entity.User;
import com.example.demo.dto.GameState;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ChallengeSessionRepository;
import com.example.demo.repository.DestinationDetailsRepository;
import com.example.demo.repository.GameSessionRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final DestinationDetailsRepository destinationDetailsRepository;
    private final ChallengeSessionRepository challengeSessionRepository;
    private final UserRepository userRepository;

    private final Map<Long, GameState> activeSessions = new ConcurrentHashMap<>();

    public Long startGame(Long userId, int totalQuestions, int timePerQuestion) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) throw new IllegalArgumentException("User not found");

        GameSession session = GameSession.builder()
                .user(userOpt.get())
                .playedAt(LocalDateTime.now())
                .scoreThisGame(0)
                .correctCount(0)
                .incorrectCount(0)
                .build();
        session = gameSessionRepository.save(session);

        GameState gameState = GameState.builder()
                .totalScore(0)
                .correctCount(0)
                .incorrectCount(0)
                .totalQuestions(totalQuestions)
                .timeLimitPerQuestion(timePerQuestion)
                .clueTaken(0)
                .startedAt(LocalDateTime.now())
                .build();

        activeSessions.put(session.getId(), gameState);
        return session.getId();
    }

    public DestinationDetails getRandomQuestion() {
        Long maxId = destinationDetailsRepository.getMaxId();
        if (maxId == null || maxId < 1) throw new ResourceNotFoundException("No questions available.");

        DestinationDetails question = null;
        int attempts = 0;
        while (question == null && attempts < 10) {
            long randomId = 1 + (long) (Math.random() * maxId);
            question = destinationDetailsRepository.findById(randomId).orElse(null);
            attempts++;
        }

        if (question == null) throw new ResourceNotFoundException("Failed to fetch a random question.");
        return question;
    }

    public QuestionResponseDto getQuestionWithOptions() {
        DestinationDetails correct = getRandomQuestion();
        List<DestinationDetails> all = destinationDetailsRepository.findAll();

        // Prepare 3 wrong options
        List<String> wrongCities = all.stream()
                .filter(d -> !d.getCity().equalsIgnoreCase(correct.getCity()))
                .map(DestinationDetails::getCity)
                .distinct()
                .limit(50)
                .collect(Collectors.toList());

        Collections.shuffle(wrongCities);
        List<String> options = new ArrayList<>(wrongCities.subList(0, 3));
        options.add(correct.getCity());
        Collections.shuffle(options);

        // Get the first clue
        String firstClue = correct.getClues().isEmpty() ? "No clues available" : correct.getClues().get(0);

        return new QuestionResponseDto(
                correct.getId(),
                0,
                correct.getClues().size(),
                firstClue,
                options
        );
    }

    public String getClue(Long questionId, int clueLevel, Long sessionId) {
        DestinationDetails question = destinationDetailsRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        List<String> clues = question.getClues();
        if (clueLevel >= clues.size()) {
            throw new IllegalArgumentException("No more clues available");
        }

        GameState state = activeSessions.get(sessionId);
        if (state == null) {
            throw new IllegalArgumentException("Invalid or expired session");
        }

        // Increment the number of clues taken
        state.setClueTaken(state.getClueTaken() + 1);

        return clues.get(clueLevel);
    }

    public void endGame(Long sessionId) {
        GameState state = activeSessions.remove(sessionId);
        if (state == null) throw new IllegalArgumentException("Invalid or expired session");

        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found"));

        session.setScoreThisGame(state.getTotalScore());
        session.setCorrectCount(state.getCorrectCount());
        session.setIncorrectCount(state.getIncorrectCount());
        gameSessionRepository.save(session);

        User user = session.getUser();
        user.setTotalScore(user.getTotalScore() + state.getTotalScore());
        userRepository.save(user);
    }

    public AnswerResponseDto evaluateAnswer(Long sessionId, Long questionId, String selectedCity, int timeTaken) {
        GameState state = activeSessions.get(sessionId);
        if (state == null) throw new IllegalArgumentException("Invalid or expired session");

        DestinationDetails question = destinationDetailsRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        boolean isCorrect = question.getCity().equalsIgnoreCase(selectedCity);

        int basePoints = 100;
        int cluePenalty = 25 * state.getClueTaken(); // 25-point penalty per clue
        int adjustedBase = Math.max(0, basePoints - cluePenalty);

        int timeLimit = state.getTimeLimitPerQuestion();
        int timeLeft = Math.max(0, timeLimit - timeTaken);
        int maxTimeBonus = 50;
        int timeBonus = (int) ((timeLeft / (double) timeLimit) * maxTimeBonus);

        int pointsEarned = isCorrect ? adjustedBase + timeBonus : 0;

        if (isCorrect) {
            state.setCorrectCount(state.getCorrectCount() + 1);
        } else {
            state.setIncorrectCount(state.getIncorrectCount() + 1);
        }

        state.setTotalScore(state.getTotalScore() + pointsEarned);
        state.setClueTaken(0); // reset clue count

        return AnswerResponseDto.builder()
                .correct(isCorrect)
                .pointsEarned(pointsEarned)
                .totalScore(state.getTotalScore())
                .correctCount(state.getCorrectCount())
                .incorrectCount(state.getIncorrectCount())
                .funFacts(question.getFunFacts())  // assuming you have this field in DestinationDetails
                .build();
    }

    public String createChallenge(String inviterUsername, String inviteeUsername, int totalQuestions, int timePerQuestion) {
        User inviter = userRepository.findByUsername(inviterUsername)
                .orElseThrow(() -> new IllegalArgumentException("Inviter not found"));

        User invitee = userRepository.findByUsername(inviteeUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invitee not found"));

        // Start a session for the inviter
        Long inviterSessionId = startGame(inviter.getId(), totalQuestions, timePerQuestion);

        ChallengeSession challenge = ChallengeSession.builder()
                .inviter(inviter)
                .invitee(invitee)
                .challengeCode(UUID.randomUUID().toString().substring(0, 8))
                .accepted(false)
                .inviterGameSessionId(inviterSessionId)
                .build();

        challengeSessionRepository.save(challenge);
        return challenge.getChallengeCode();
    }

    public void acceptChallenge(String challengeCode) {
        ChallengeSession challenge = challengeSessionRepository.findByChallengeCode(challengeCode)
                .orElseThrow(() -> new IllegalArgumentException("Challenge not found"));

        if (challenge.isAccepted()) {
            throw new IllegalStateException("Challenge already accepted");
        }

        User invitee = challenge.getInvitee();

        // Start session for invitee
        Long inviteeSessionId = startGame(invitee.getId(), activeSessions.get(challenge.getInviterGameSessionId()).getTotalQuestions(),
                activeSessions.get(challenge.getInviterGameSessionId()).getTimeLimitPerQuestion());

        challenge.setInviteeGameSessionId(inviteeSessionId);
        challenge.setAccepted(true);

        challengeSessionRepository.save(challenge);
    }

}