package com.thropic.talki.transcription.domain.event;

import java.time.Instant;
import java.util.UUID;

public class TranscriptionCompletedEvent {
    private String eventId;
    private String eventType = "transcription.completed";
    private Instant occurredAt;
    private String traceId;
    private String version = "1.0";
    private String sessionId;
    private String userId;
    private String transcriptionText;
    private int wordCount;
    private int durationSeconds;

    public TranscriptionCompletedEvent() {}

    public TranscriptionCompletedEvent(String sessionId, String userId,
                                        String transcriptionText, int wordCount,
                                        int durationSeconds, String traceId) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
        this.traceId = traceId;
        this.sessionId = sessionId;
        this.userId = userId;
        this.transcriptionText = transcriptionText;
        this.wordCount = wordCount;
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
    public String getTranscriptionText() { return transcriptionText; }
    public void setTranscriptionText(String transcriptionText) { this.transcriptionText = transcriptionText; }
    public int getWordCount() { return wordCount; }
    public void setWordCount(int wordCount) { this.wordCount = wordCount; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }
}
