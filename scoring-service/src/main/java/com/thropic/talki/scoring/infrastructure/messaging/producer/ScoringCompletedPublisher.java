package com.thropic.talki.scoring.infrastructure.messaging.producer;

import com.thropic.talki.scoring.domain.event.ScoringCompletedEvent;
import com.thropic.talki.scoring.infrastructure.messaging.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ScoringCompletedPublisher {

    private static final Logger log = LoggerFactory.getLogger(ScoringCompletedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public ScoringCompletedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(ScoringCompletedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.OUT_ROUTING,
                event
        );
        log.info("[scoring-service] Published scoring.completed — sessionId={} overall={}",
                event.getSessionId(), event.getOverallScore());
    }
}
