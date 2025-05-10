// repository/GameSessionRepository.java
package com.example.demo.repository;

import com.example.demo.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    List<GameSession> findTop5ByUserIdOrderByPlayedAtDesc(Long userId);
}
