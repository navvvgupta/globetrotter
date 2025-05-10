package com.example.demo.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User inviter;

    @ManyToOne
    private User invitee;

    private String challengeCode;

    private boolean accepted;

    private Long inviterGameSessionId;

    private Long inviteeGameSessionId;
}
