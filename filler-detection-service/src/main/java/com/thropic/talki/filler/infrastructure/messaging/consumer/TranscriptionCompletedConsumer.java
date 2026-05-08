package com.thropic.talki.filler.infrastructure.messaging.consumer;

import com.thropic.talki.filler.domain.event.FillerAnalyzedEvent;
import com.thropic.talki.filler.domain.event.TranscriptionCompletedEvent;
import com.thropic.talki.filler.domain.model.FillerDetector;
import com.thropic.talki.filler.infrastructure.messaging.config.RabbitMQConfig;
import com.thropic.talki.filler.infrastructure.messaging.producer.FillerAnalyzedPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TranscriptionCompletedConsumer {

    private static final Logger log = LoggerFactory.getLogger(TranscriptionCompletedConsumer.class);

    private final FillerDetector fillerDetector;
    private final FillerAnalyzedPublisher publisher;

    public TranscriptionCompletedConsumer(FillerDetector fillerDetector,
                                           FillerAnalyzedPublisher publisher) {
        this.fillerDetector = fillerDetector;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handle(TranscriptionCompletedEvent event) {
        log.info("[filler-detection-service] Received transcription.completed — sessionId={}",
                event.getSessionId());

        Map<String, Integer> fillersByType = fillerDetector.detect(event.getTranscriptionText());
        int total = fillerDetector.totalFillers(fillersByType);

        log.info("[filler-detection-service] Detected {} fillers — sessionId={} types={}",
                total, event.getSessionId(), fillersByType.keySet());

        FillerAnalyzedEvent analyzed = new FillerAnalyzedEvent(
                event.getSessionId(),
                event.getUserId(),
                total,
                fillersByType,
                event.getWordCount(),
                event.getDurationSeconds(),
                event.getTraceId()
        );

        publisher.publish(analyzed);
    }
}
