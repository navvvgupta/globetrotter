package com.example.demo.repository;

import com.example.demo.entity.ChallengeSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeSessionRepository extends JpaRepository<ChallengeSession, Long> {
    Optional<ChallengeSession> findByChallengeCode(String challengeCode);
}
