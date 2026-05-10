package com.thropic.talki.filler.domain.event;

import java.time.Instant;

public class SessionLiveFinalizedEvent {
    private String eventId;
    private String eventType;
    private Instant occurredAt;
    private String traceId;
    private String version;
    private String sessionId;
    private String userId;
    private String mode;
    private String scenarioId;
    private String transcriptGemini;
    private int wordsPerMinute;
    private double silenceRatio;
    private double volumeRmsAvg;
    private int durationSeconds;
    private String academicSegment;

    public SessionLiveFinalizedEvent() {}

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
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getScenarioId() { return scenarioId; }
    public void setScenarioId(String scenarioId) { this.scenarioId = scenarioId; }
    public String getTranscriptGemini() { return transcriptGemini; }
    public void setTranscriptGemini(String transcriptGemini) { this.transcriptGemini = transcriptGemini; }
    public int getWordsPerMinute() { return wordsPerMinute; }
    public void setWordsPerMinute(int wordsPerMinute) { this.wordsPerMinute = wordsPerMinute; }
    public double getSilenceRatio() { return silenceRatio; }
    public void setSilenceRatio(double silenceRatio) { this.silenceRatio = silenceRatio; }
    public double getVolumeRmsAvg() { return volumeRmsAvg; }
    public void setVolumeRmsAvg(double volumeRmsAvg) { this.volumeRmsAvg = volumeRmsAvg; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
    public String getAcademicSegment() { return academicSegment; }
    public void setAcademicSegment(String academicSegment) { this.academicSegment = academicSegment; }
}
