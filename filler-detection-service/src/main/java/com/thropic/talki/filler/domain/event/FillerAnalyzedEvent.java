package com.thropic.talki.filler.domain.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class FillerAnalyzedEvent {
    private String eventId;
    private String eventType = "fillers.analyzed";
    private Instant occurredAt;
    private String traceId;
    private String version = "1.0";
    private String sessionId;
    private String userId;
    private int totalFillers;
    private Map<String, Integer> fillersByType;
    private int wordCount;
    private int wordsPerMinute;
    private double silenceRatio;
    private double volumeRmsAvg;
    private int durationSeconds;

    public FillerAnalyzedEvent() {}

    public FillerAnalyzedEvent(String sessionId, String userId, int totalFillers,
                                Map<String, Integer> fillersByType,
                                int wordCount, int wordsPerMinute,
                                double silenceRatio, double volumeRmsAvg,
                                int durationSeconds, String traceId) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
        this.traceId = traceId;
        this.sessionId = sessionId;
        this.userId = userId;
        this.totalFillers = totalFillers;
        this.fillersByType = fillersByType;
        this.wordCount = wordCount;
        this.wordsPerMinute = wordsPerMinute;
        this.silenceRatio = silenceRatio;
        this.volumeRmsAvg = volumeRmsAvg;
        this.durationSeconds = durationSeconds;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public int getTotalFillers() { return totalFillers; }
    public void setTotalFillers(int totalFillers) { this.totalFillers = totalFillers; }
    public Map<String, Integer> getFillersByType() { return fillersByType; }
    public void setFillersByType(Map<String, Integer> fillersByType) { this.fillersByType = fillersByType; }
    public int getWordCount() { return wordCount; }
    public void setWordCount(int wordCount) { this.wordCount = wordCount; }
    public int getWordsPerMinute() { return wordsPerMinute; }
    public void setWordsPerMinute(int wordsPerMinute) { this.wordsPerMinute = wordsPerMinute; }
    public double getSilenceRatio() { return silenceRatio; }
    public void setSilenceRatio(double silenceRatio) { this.silenceRatio = silenceRatio; }
    public double getVolumeRmsAvg() { return volumeRmsAvg; }
    public void setVolumeRmsAvg(double volumeRmsAvg) { this.volumeRmsAvg = volumeRmsAvg; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
}
