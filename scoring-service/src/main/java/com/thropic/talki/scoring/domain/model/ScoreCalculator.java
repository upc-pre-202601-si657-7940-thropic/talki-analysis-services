package com.thropic.talki.scoring.domain.model;

import com.thropic.talki.scoring.domain.event.FillerAnalyzedEvent;
import com.thropic.talki.scoring.domain.event.ScoringCompletedEvent;
import org.springframework.stereotype.Component;

@Component
public class ScoreCalculator {

    public ScoringCompletedEvent calculate(FillerAnalyzedEvent event) {
        int wpm = computeWpm(event.getWordCount(), event.getDurationSeconds());
        double silenceRatio = 0.08; // placeholder: análisis acústico fuera del alcance JVM

        int fluency    = fluencyScore(wpm, event.getTotalFillers());
        int clarity    = clarityScore(wpm);
        int volume     = 70; // análisis acústico del audio (placeholder)
        int vocabulary = vocabularyScore(event.getTotalFillers());
        int confidence = confidenceScore(silenceRatio);

        VoiceScore scores = new VoiceScore(fluency, clarity, volume, vocabulary, confidence);

        return new ScoringCompletedEvent(
                event.getSessionId(),
                event.getUserId(),
                scores,
                event.getTotalFillers(),
                silenceRatio,
                wpm,
                event.getTraceId()
        );
    }

    private int computeWpm(int wordCount, int durationSeconds) {
        if (durationSeconds <= 0) return 0;
        return (wordCount * 60) / durationSeconds;
    }

    private int fluencyScore(int wpm, int fillerCount) {
        int base = (wpm >= 120 && wpm <= 160) ? 100 :
                   (wpm >= 100) ? 80 : 60;
        return Math.max(0, base - fillerCount * 2);
    }

    private int clarityScore(int wpm) {
        if (wpm >= 100 && wpm <= 150) return 85;
        if (wpm >= 80) return 65;
        return 50;
    }

    private int vocabularyScore(int fillerCount) {
        return Math.max(0, 100 - fillerCount * 3);
    }

    private int confidenceScore(double silenceRatio) {
        if (silenceRatio < 0.05) return 90;
        if (silenceRatio < 0.10) return 75;
        if (silenceRatio < 0.20) return 55;
        return 30;
    }
}
