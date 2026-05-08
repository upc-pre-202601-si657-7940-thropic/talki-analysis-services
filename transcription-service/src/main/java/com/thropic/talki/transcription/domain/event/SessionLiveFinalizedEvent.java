package com.thropic.talki.transcription.domain.event;

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
    private String audioUri;
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
    public String getAudioUri() { return audioUri; }
    public void setAudioUri(String audioUri) { this.audioUri = audioUri; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
    public String getAcademicSegment() { return academicSegment; }
    public void setAcademicSegment(String academicSegment) { this.academicSegment = academicSegment; }
}
