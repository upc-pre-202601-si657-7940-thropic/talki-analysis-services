package com.thropic.talki.scoring.infrastructure.persistence;

import com.thropic.talki.scoring.domain.model.ScoreResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreResultRepository extends JpaRepository<ScoreResult, Long> {
    Optional<ScoreResult> findBySessionId(String sessionId);
}
