package com.thropic.talki.filler.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FillerDetectorTest {

    private FillerDetector fillerDetector;

    @BeforeEach
    void setUp() {
        fillerDetector = new FillerDetector();
    }

    @Test
    void detect_whenTranscriptHasMultipleFillers_shouldCountEachType() {
        String transcript = "Este, bueno, lo que quiero decir es que, eh, este proyecto, o sea, " +
                "básicamente busca, eh, mejorar las cosas.";

        Map<String, Integer> result = fillerDetector.detect(transcript);

        assertThat(result).containsKeys("este", "bueno", "eh", "o sea", "básicamente");
        assertThat(result.get("eh")).isEqualTo(2);
    }

    @Test
    void detect_whenTranscriptHasNoFillers_shouldReturnEmptyMap() {
        String transcript = "Mi proyecto consiste en una aplicación web moderna.";

        Map<String, Integer> result = fillerDetector.detect(transcript);

        assertThat(result).isEmpty();
    }

    @Test
    void detect_shouldBeCaseInsensitive() {
        String transcript = "BUENO, este Es importante, ENTONCES, vamos a empezar.";

        Map<String, Integer> result = fillerDetector.detect(transcript);

        assertThat(result).containsKey("bueno");
        assertThat(result).containsKey("entonces");
    }

    @Test
    void detect_shouldMatchOnlyWholeWords() {
        // "bueno" matchea, "buenos" NO debería contar
        String transcript = "Buenos días, bueno, comencemos.";

        Map<String, Integer> result = fillerDetector.detect(transcript);

        assertThat(result.get("bueno")).isEqualTo(1);
    }

    @Test
    void totalFillers_shouldSumAllOccurrences() {
        Map<String, Integer> counts = Map.of("este", 3, "eh", 2, "bueno", 1);

        int total = fillerDetector.totalFillers(counts);

        assertThat(total).isEqualTo(6);
    }

    @Test
    void totalFillers_whenEmpty_shouldReturnZero() {
        assertThat(fillerDetector.totalFillers(Map.of())).isZero();
    }

    @Test
    void detect_whenTranscriptIsEmpty_shouldReturnEmptyMap() {
        assertThat(fillerDetector.detect("")).isEmpty();
    }
}
