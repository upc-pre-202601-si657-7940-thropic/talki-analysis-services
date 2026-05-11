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

    private FillerAnalyzedEvent buildEvent(int wordCount, int wordsPerMinute,
                                            double silenceRatio, double volumeRmsAvg,
                                            int durationSeconds, int totalFillers) {
        FillerAnalyzedEvent e = new FillerAnalyzedEvent();
        e.setSessionId("sess-1");
        e.setUserId("user-1");
        e.setTotalFillers(totalFillers);
        e.setFillersByType(Map.of("este", totalFillers));
        e.setWordCount(wordCount);
        e.setWordsPerMinute(wordsPerMinute);
        e.setSilenceRatio(silenceRatio);
        e.setVolumeRmsAvg(volumeRmsAvg);
        e.setDurationSeconds(durationSeconds);
        e.setTraceId("trace-1");
        return e;
    }

    @Test
    void calculate_shouldUseWpmFromUpstreamEventNotRecalculate() {
        // upstream provee wpm=150, pero wordCount/durationSeconds daría 100;
        // el calculator debe respetar la fuente upstream.
        FillerAnalyzedEvent event = buildEvent(100, 150, 0.05, 0.6, 60, 0);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getWordsPerMinute()).isEqualTo(150);
        assertThat(result.getScores().getFluency()).isGreaterThanOrEqualTo(80);
    }

    @Test
    void calculate_whenWpmFromUpstreamIsZero_shouldEstimateFromWordCount() {
        // Fallback: si upstream no provee wpm, derivar de wordCount/duration.
        FillerAnalyzedEvent event = buildEvent(300, 0, 0.08, 0.6, 120, 2);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getWordsPerMinute()).isEqualTo(150);
    }

    @Test
    void calculate_whenWpmIsLow_shouldPenalizeClarity() {
        FillerAnalyzedEvent event = buildEvent(100, 50, 0.08, 0.6, 120, 0);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getScores().getClarity()).isEqualTo(50);
    }

    @Test
    void calculate_whenManyFillers_shouldReduceVocabularyAndFluency() {
        FillerAnalyzedEvent event = buildEvent(300, 150, 0.08, 0.6, 120, 20);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getScores().getVocabulary()).isEqualTo(40);
        assertThat(result.getScores().getFluency()).isLessThan(80);
    }

    @Test
    void calculate_whenDurationIsZero_shouldNotDivideByZero() {
        FillerAnalyzedEvent event = buildEvent(50, 0, 0.0, 0.0, 0, 0);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getWordsPerMinute()).isZero();
    }

    @Test
    void calculate_volumeScore_shouldUseRmsFromUpstream() {
        // RMS óptimo 0.5-0.8 → score 90
        FillerAnalyzedEvent event = buildEvent(200, 130, 0.08, 0.65, 90, 1);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getScores().getVolume()).isEqualTo(90);
    }

    @Test
    void calculate_volumeScore_whenRmsIsLow_shouldGiveLowVolumeScore() {
        FillerAnalyzedEvent event = buildEvent(200, 130, 0.08, 0.2, 90, 1);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getScores().getVolume()).isEqualTo(40);
    }

    @Test
    void calculate_volumeScore_whenRmsIsZero_shouldReturnZero() {
        FillerAnalyzedEvent event = buildEvent(200, 130, 0.08, 0.0, 90, 1);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getScores().getVolume()).isZero();
    }

    @Test
    void calculate_confidenceScore_shouldReflectSilenceRatioFromUpstream() {
        // silenceRatio < 0.05 → confidence = 90
        FillerAnalyzedEvent event = buildEvent(200, 130, 0.03, 0.6, 90, 1);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getScores().getConfidence()).isEqualTo(90);
        assertThat(result.getSilenceRatio()).isEqualTo(0.03);
    }

    @Test
    void calculate_shouldPopulateAllFiveDimensions() {
        FillerAnalyzedEvent event = buildEvent(200, 130, 0.08, 0.6, 90, 5);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getScores().getFluency()).isBetween(0, 100);
        assertThat(result.getScores().getClarity()).isBetween(0, 100);
        assertThat(result.getScores().getVolume()).isBetween(0, 100);
        assertThat(result.getScores().getVocabulary()).isBetween(0, 100);
        assertThat(result.getScores().getConfidence()).isBetween(0, 100);
    }

    @Test
    void calculate_overallScoreShouldBeAverageOfFiveDimensions() {
        FillerAnalyzedEvent event = buildEvent(280, 140, 0.08, 0.6, 120, 3);

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
        FillerAnalyzedEvent event = buildEvent(200, 130, 0.08, 0.6, 100, 2);

        ScoringCompletedEvent result = scoreCalculator.calculate(event);

        assertThat(result.getTraceId()).isEqualTo("trace-1");
        assertThat(result.getSessionId()).isEqualTo("sess-1");
    }
}
