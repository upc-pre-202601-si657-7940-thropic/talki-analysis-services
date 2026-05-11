package com.thropic.talki.scoring.domain.event;

import java.time.Instant;
import java.util.Map;

public class FillerAnalyzedEvent {
    private String eventId;
    private String eventType;
    private Instant occurredAt;
    private String traceId;
    private String version;
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
