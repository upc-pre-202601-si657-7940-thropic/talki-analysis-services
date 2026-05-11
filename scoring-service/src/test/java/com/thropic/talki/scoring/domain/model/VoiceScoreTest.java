package com.thropic.talki.scoring.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class VoiceScoreTest {

    @Test
    void constructor_whenAllDimensionsAreValid_shouldBuildSuccessfully() {
        VoiceScore score = new VoiceScore(80, 75, 70, 85, 90);

        assertThat(score.getFluency()).isEqualTo(80);
        assertThat(score.getClarity()).isEqualTo(75);
        assertThat(score.getVolume()).isEqualTo(70);
        assertThat(score.getVocabulary()).isEqualTo(85);
        assertThat(score.getConfidence()).isEqualTo(90);
    }

    @Test
    void constructor_whenFluencyIsNegative_shouldThrow() {
        assertThatThrownBy(() -> new VoiceScore(-1, 75, 70, 85, 90))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fluency");
    }

    @Test
    void constructor_whenClarityIsAbove100_shouldThrow() {
        assertThatThrownBy(() -> new VoiceScore(80, 101, 70, 85, 90))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("clarity");
    }

    @Test
    void constructor_whenVolumeIsAtUpperBoundary_shouldAccept() {
        VoiceScore score = new VoiceScore(80, 75, 100, 85, 90);

        assertThat(score.getVolume()).isEqualTo(100);
    }

    @Test
    void constructor_whenVocabularyIsAtLowerBoundary_shouldAccept() {
        VoiceScore score = new VoiceScore(80, 75, 70, 0, 90);

        assertThat(score.getVocabulary()).isZero();
    }

    @Test
    void constructor_whenConfidenceIsAbove100_shouldThrow() {
        assertThatThrownBy(() -> new VoiceScore(80, 75, 70, 85, 200))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("confidence");
    }

    @Test
    void overall_shouldReturnArithmeticMeanOfFiveDimensions() {
        VoiceScore score = new VoiceScore(60, 70, 80, 90, 100);

        assertThat(score.overall()).isEqualTo(80);
    }

    @Test
    void overall_whenAllZero_shouldReturnZero() {
        VoiceScore score = new VoiceScore(0, 0, 0, 0, 0);

        assertThat(score.overall()).isZero();
    }

    @Test
    void overall_whenAllPerfect_shouldReturn100() {
        VoiceScore score = new VoiceScore(100, 100, 100, 100, 100);

        assertThat(score.overall()).isEqualTo(100);
    }

    @Test
    void overall_shouldTruncateRatherThanRound() {
        // (50 + 50 + 50 + 50 + 51) / 5 = 251/5 = 50 (truncado, no 50.2)
        VoiceScore score = new VoiceScore(50, 50, 50, 50, 51);

        assertThat(score.overall()).isEqualTo(50);
    }

    @Test
    void noArgsConstructor_shouldProduceZeroedScoreForJacksonDeserialization() {
        // El constructor sin args existe para que Jackson pueda deserializar el
        // VoiceScore desde el payload del evento RabbitMQ. Los campos quedan en
        // 0 hasta que los setters los pueblen.
        VoiceScore score = new VoiceScore();

        assertThat(score.getFluency()).isZero();
        assertThat(score.overall()).isZero();
    }

    @Test
    void setters_shouldUpdateDimensionsForDeserialization() {
        VoiceScore score = new VoiceScore();

        score.setFluency(80);
        score.setClarity(70);
        score.setVolume(60);
        score.setVocabulary(85);
        score.setConfidence(90);

        // Los setters no validan (responsabilidad del deserializador upstream);
        // solo el constructor con args refuerza el invariante 0-100.
        assertThat(score.overall()).isEqualTo(77);
    }
}
