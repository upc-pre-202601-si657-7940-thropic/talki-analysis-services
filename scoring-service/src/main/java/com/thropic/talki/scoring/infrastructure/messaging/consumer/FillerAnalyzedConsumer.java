package com.thropic.talki.scoring.infrastructure.messaging.consumer;

import com.thropic.talki.scoring.domain.event.FillerAnalyzedEvent;
import com.thropic.talki.scoring.domain.event.ScoringCompletedEvent;
import com.thropic.talki.scoring.domain.model.ScoreCalculator;
import com.thropic.talki.scoring.infrastructure.messaging.config.RabbitMQConfig;
import com.thropic.talki.scoring.infrastructure.messaging.producer.ScoringCompletedPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FillerAnalyzedConsumer {

    private static final Logger log = LoggerFactory.getLogger(FillerAnalyzedConsumer.class);

    private final ScoreCalculator scoreCalculator;
    private final ScoringCompletedPublisher publisher;

    public FillerAnalyzedConsumer(ScoreCalculator scoreCalculator,
                                   ScoringCompletedPublisher publisher) {
        this.scoreCalculator = scoreCalculator;
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handle(FillerAnalyzedEvent event) {
        log.info("[scoring-service] Received fillers.analyzed — sessionId={} totalFillers={}",
                event.getSessionId(), event.getTotalFillers());

        ScoringCompletedEvent scoring = scoreCalculator.calculate(event);

        log.info("[scoring-service] Score calculated — sessionId={} overall={} fluency={} clarity={} vocabulary={} confidence={}",
                event.getSessionId(), scoring.getOverallScore(),
                scoring.getScores().getFluency(), scoring.getScores().getClarity(),
                scoring.getScores().getVocabulary(), scoring.getScores().getConfidence());

        publisher.publish(scoring);
    }
}
