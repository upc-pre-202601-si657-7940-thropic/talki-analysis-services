package com.thropic.talki.filler.infrastructure.messaging.consumer;

import com.thropic.talki.filler.domain.event.FillerAnalyzedEvent;
import com.thropic.talki.filler.domain.event.SessionLiveFinalizedEvent;
import com.thropic.talki.filler.domain.model.FillerDetector;
import com.thropic.talki.filler.infrastructure.messaging.producer.FillerAnalyzedPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionLiveFinalizedConsumerTest {

    @Mock
    private FillerDetector fillerDetector;

    @Mock
    private FillerAnalyzedPublisher publisher;

    @InjectMocks
    private SessionLiveFinalizedConsumer consumer;

    private SessionLiveFinalizedEvent inputEvent;

    @BeforeEach
    void setUp() {
        inputEvent = new SessionLiveFinalizedEvent();
        inputEvent.setSessionId("sess-1");
        inputEvent.setUserId("user-1");
        inputEvent.setMode("quick_practice");
        inputEvent.setTranscriptGemini("Este es mi proyecto, eh, sobre comunicación oral, este.");
        inputEvent.setWordsPerMinute(95);
        inputEvent.setSilenceRatio(0.15);
        inputEvent.setVolumeRmsAvg(0.6);
        inputEvent.setDurationSeconds(300);
        inputEvent.setAcademicSegment("ciclos_6_10");
        inputEvent.setTraceId("trace-1");
    }

    @Test
    void handle_shouldDetectFillersFromTranscriptGeminiAndPublish() {
        Map<String, Integer> detected = Map.of("este", 2, "eh", 1);
        when(fillerDetector.detect("Este es mi proyecto, eh, sobre comunicación oral, este."))
                .thenReturn(detected);
        when(fillerDetector.totalFillers(detected)).thenReturn(3);

        consumer.handle(inputEvent);

        ArgumentCaptor<FillerAnalyzedEvent> captor =
                ArgumentCaptor.forClass(FillerAnalyzedEvent.class);
        verify(publisher).publish(captor.capture());
        FillerAnalyzedEvent published = captor.getValue();

        assertThat(published.getSessionId()).isEqualTo("sess-1");
        assertThat(published.getUserId()).isEqualTo("user-1");
        assertThat(published.getTotalFillers()).isEqualTo(3);
        assertThat(published.getFillersByType()).containsEntry("este", 2);
        assertThat(published.getTraceId()).isEqualTo("trace-1");
        assertThat(published.getDurationSeconds()).isEqualTo(300);
    }

    @Test
    void handle_whenTranscriptIsNull_shouldNotFailAndProcessEmptyString() {
        inputEvent.setTranscriptGemini(null);
        when(fillerDetector.detect("")).thenReturn(Map.of());
        when(fillerDetector.totalFillers(Map.of())).thenReturn(0);

        consumer.handle(inputEvent);

        verify(fillerDetector).detect("");
        verify(publisher).publish(any(FillerAnalyzedEvent.class));
    }

    @Test
    void handle_shouldComputeWordCountFromTranscript() {
        inputEvent.setTranscriptGemini("uno dos tres cuatro cinco");
        when(fillerDetector.detect("uno dos tres cuatro cinco")).thenReturn(Map.of());
        when(fillerDetector.totalFillers(Map.of())).thenReturn(0);

        consumer.handle(inputEvent);

        ArgumentCaptor<FillerAnalyzedEvent> captor =
                ArgumentCaptor.forClass(FillerAnalyzedEvent.class);
        verify(publisher).publish(captor.capture());
        assertThat(captor.getValue().getWordCount()).isEqualTo(5);
    }

    @Test
    void handle_whenTranscriptIsBlank_shouldReportZeroWords() {
        inputEvent.setTranscriptGemini("   ");
        when(fillerDetector.detect(anyString())).thenReturn(Map.of());
        when(fillerDetector.totalFillers(Map.of())).thenReturn(0);

        consumer.handle(inputEvent);

        ArgumentCaptor<FillerAnalyzedEvent> captor =
                ArgumentCaptor.forClass(FillerAnalyzedEvent.class);
        verify(publisher).publish(captor.capture());
        assertThat(captor.getValue().getWordCount()).isZero();
    }
}
