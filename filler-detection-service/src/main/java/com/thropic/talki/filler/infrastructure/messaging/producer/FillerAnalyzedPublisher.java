package com.thropic.talki.filler.infrastructure.messaging.producer;

import com.thropic.talki.filler.domain.event.FillerAnalyzedEvent;
import com.thropic.talki.filler.infrastructure.messaging.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class FillerAnalyzedPublisher {

    private static final Logger log = LoggerFactory.getLogger(FillerAnalyzedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public FillerAnalyzedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(FillerAnalyzedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.OUT_ROUTING,
                event
        );
        log.info("[filler-detection-service] Published fillers.analyzed — sessionId={} total={}",
                event.getSessionId(), event.getTotalFillers());
    }
}
