package com.thropic.talki.filler.infrastructure.messaging.consumer;

import com.thropic.talki.filler.domain.event.FillerAnalyzedEvent;
import com.thropic.talki.filler.domain.event.SessionLiveFinalizedEvent;
import com.thropic.talki.filler.domain.model.FillerDetector;
import com.thropic.talki.filler.infrastructure.messaging.config.RabbitMQConfig;
import com.thropic.talki.filler.infrastructure.messaging.producer.FillerAnalyzedPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SessionLiveFinalizedConsumer {

    private static final Logger log = LoggerFactory.getLogger(SessionLiveFinalizedConsumer.class);

    private final FillerDetector fillerDetector;
    private final FillerAnalyzedPublisher publisher;

    public SessionLiveFinalizedConsumer(FillerDetector fillerDetector,
                                         FillerAnalyzedPublisher publisher) {
        this.fillerDetector = fillerDetector;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handle(SessionLiveFinalizedEvent event) {
        log.info("[filler-detection-service] Received session.live.finalized — sessionId={} mode={}",
                event.getSessionId(), event.getMode());

        String transcript = event.getTranscriptGemini() == null ? "" : event.getTranscriptGemini();
        Map<String, Integer> fillersByType = fillerDetector.detect(transcript);
        int total = fillerDetector.totalFillers(fillersByType);
        int wordCount = transcript.isBlank() ? 0 : transcript.trim().split("\\s+").length;

        log.info("[filler-detection-service] Detected {} fillers — sessionId={} types={}",
                total, event.getSessionId(), fillersByType.keySet());

        FillerAnalyzedEvent analyzed = new FillerAnalyzedEvent(
                event.getSessionId(),
                event.getUserId(),
                total,
                fillersByType,
                wordCount,
                event.getDurationSeconds(),
                event.getTraceId()
        );

        publisher.publish(analyzed);
    }
}
