package com.thropic.talki.scoring.domain.model;

import com.thropic.talki.scoring.domain.event.FillerAnalyzedEvent;
import com.thropic.talki.scoring.domain.event.ScoringCompletedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ScoreCalculatorTest {

    private ScoreCalculator scoreCalculator;

    @BeforeEach
    void setUp() {
        scoreCalculator = new ScoreCalculator();
    }

    private FillerAnalyzedEvent buildEvent(int wordCount, int durationSeconds, int totalFillers) {
        return new FillerAnalyzedEvent(
                "sess-1", "user-1", totalFillers,
                Map.of("este", totalFillers),
                wordCount, durationSeconds, "trace-1"
        );
    }

    @Test
    void calculate_whenSpeechIsFluent_shouldGiveHighFluencyScore() {
        FillerAnalyzedEvent event = buildEvent(300, 120, 2);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getWordsPerMinute()).isEqualTo(150);
        assertThat(result.getScores().getFluency()).isGreaterThanOrEqualTo(80);
    }

    @Test
    void calculate_whenWpmIsLow_shouldPenalizeClarity() {
        FillerAnalyzedEvent event = buildEvent(100, 120, 0);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getWordsPerMinute()).isEqualTo(50);
        assertThat(result.getScores().getClarity()).isEqualTo(50);
    }

    @Test
    void calculate_whenManyFillers_shouldReduceVocabularyAndFluency() {
        FillerAnalyzedEvent event = buildEvent(300, 120, 20);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getScores().getVocabulary()).isEqualTo(40);
        assertThat(result.getScores().getFluency()).isLessThan(80);
    }

    @Test
    void calculate_whenDurationIsZero_shouldNotDivideByZero() {
        FillerAnalyzedEvent event = buildEvent(50, 0, 0);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getWordsPerMinute()).isEqualTo(0);
    }

    @Test
    void calculate_shouldPopulateAllFiveDimensions() {
        FillerAnalyzedEvent event = buildEvent(200, 100, 5);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getScores().getFluency()).isBetween(0, 100);
        assertThat(result.getScores().getClarity()).isBetween(0, 100);
        assertThat(result.getScores().getVolume()).isBetween(0, 100);
        assertThat(result.getScores().getVocabulary()).isBetween(0, 100);
        assertThat(result.getScores().getConfidence()).isBetween(0, 100);
    }

    @Test
    void calculate_overallScoreShouldBeAverageOfFiveDimensions() {
        FillerAnalyzedEvent event = buildEvent(280, 120, 3);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        int expectedOverall = (result.getScores().getFluency()
                + result.getScores().getClarity()
                + result.getScores().getVolume()
                + result.getScores().getVocabulary()
                + result.getScores().getConfidence()) / 5;
        assertThat(result.getOverallScore()).isEqualTo(expectedOverall);
    }

    @Test
    void calculate_shouldPropagateTraceId() {
        FillerAnalyzedEvent event = buildEvent(200, 100, 2);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getTraceId()).isEqualTo("trace-1");
        assertThat(result.getSessionId()).isEqualTo("sess-1");
    }
}
