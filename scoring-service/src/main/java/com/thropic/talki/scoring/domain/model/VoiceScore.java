package com.thropic.talki.scoring.domain.model;

public class VoiceScore {
    private int fluency;
    private int clarity;
    private int volume;
    private int vocabulary;
    private int confidence;

    public VoiceScore() {}

    public VoiceScore(int fluency, int clarity, int volume, int vocabulary, int confidence) {
        validate(fluency, "fluency");
        validate(clarity, "clarity");
        validate(volume, "volume");
        validate(vocabulary, "vocabulary");
        validate(confidence, "confidence");
        this.fluency = fluency;
        this.clarity = clarity;
        this.volume = volume;
        this.vocabulary = vocabulary;
        this.confidence = confidence;
    }

    private void validate(int value, String dim) {
        if (value < 0 || value > 100)
            throw new IllegalArgumentException("Score for " + dim + " must be 0-100, got: " + value);
    }

    public int overall() {
        return (fluency + clarity + volume + vocabulary + confidence) / 5;
    }

    public int getFluency() { return fluency; }
    public void setFluency(int fluency) { this.fluency = fluency; }
    public int getClarity() { return clarity; }
    public void setClarity(int clarity) { this.clarity = clarity; }
    public int getVolume() { return volume; }
    public void setVolume(int volume) { this.volume = volume; }
    public int getVocabulary() { return vocabulary; }
    public void setVocabulary(int vocabulary) { this.vocabulary = vocabulary; }
    public int getConfidence() { return confidence; }
    public void setConfidence(int confidence) { this.confidence = confidence; }
}
