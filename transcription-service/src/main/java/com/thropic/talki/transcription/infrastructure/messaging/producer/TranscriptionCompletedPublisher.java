package com.thropic.talki.transcription.infrastructure.messaging.producer;

import com.thropic.talki.transcription.domain.event.TranscriptionCompletedEvent;
import com.thropic.talki.transcription.infrastructure.messaging.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TranscriptionCompletedPublisher {

    private static final Logger log = LoggerFactory.getLogger(TranscriptionCompletedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public TranscriptionCompletedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(TranscriptionCompletedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.OUT_ROUTING,
                event
        );
        log.info("[transcription-service] Published transcription.completed — sessionId={}",
                event.getSessionId());
    }
}
