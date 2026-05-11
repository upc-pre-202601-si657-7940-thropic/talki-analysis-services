package com.thropic.talki.scoring.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que persiste el resultado del Voice Coach Score calculado
 * por el scoring-service tras consumir fillers.analyzed. Vive en el
 * schema del scoring-service (database-per-service); su correlación
 * con la Session del bounded context Practice Session se realiza por
 * sessionId vía eventos, no por JPA cross-BC.
 */
@Entity
@Table(name = "score_results")
public class ScoreResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, unique = true)
    private String sessionId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private int fluency;

    @Column(nullable = false)
    private int clarity;

    @Column(name = "volume_score", nullable = false)
    private int volume;

    @Column(nullable = false)
    private int vocabulary;

    @Column(nullable = false)
    private int confidence;

    @Column(name = "overall_score", nullable = false)
    private int overallScore;

    @Column(name = "total_fillers", nullable = false)
    private int totalFillers;

    @Column(name = "words_per_minute", nullable = false)
    private int wordsPerMinute;

    @Column(name = "silence_ratio", nullable = false)
    private double silenceRatio;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected ScoreResult() {}

    public ScoreResult(String sessionId, String userId, VoiceScore scores,
                        int totalFillers, int wordsPerMinute, double silenceRatio) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.fluency = scores.getFluency();
        this.clarity = scores.getClarity();
        this.volume = scores.getVolume();
        this.vocabulary = scores.getVocabulary();
        this.confidence = scores.getConfidence();
        this.overallScore = scores.overall();
        this.totalFillers = totalFillers;
        this.wordsPerMinute = wordsPerMinute;
        this.silenceRatio = silenceRatio;
        this.createdAt = LocalDateTime.now();
    }

    public VoiceScore asVoiceScore() {
        return new VoiceScore(fluency, clarity, volume, vocabulary, confidence);
    }

    public Long getId() { return id; }
    public String getSessionId() { return sessionId; }
    public String getUserId() { return userId; }
    public int getFluency() { return fluency; }
    public int getClarity() { return clarity; }
    public int getVolume() { return volume; }
    public int getVocabulary() { return vocabulary; }
    public int getConfidence() { return confidence; }
    public int getOverallScore() { return overallScore; }
    public int getTotalFillers() { return totalFillers; }
    public int getWordsPerMinute() { return wordsPerMinute; }
    public double getSilenceRatio() { return silenceRatio; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
