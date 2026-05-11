package com.thropic.talki.scoring.infrastructure.messaging.consumer;

import com.thropic.talki.scoring.domain.event.FillerAnalyzedEvent;
import com.thropic.talki.scoring.domain.event.ScoringCompletedEvent;
import com.thropic.talki.scoring.domain.model.ScoreCalculator;
import com.thropic.talki.scoring.domain.model.ScoreResult;
import com.thropic.talki.scoring.domain.model.VoiceScore;
import com.thropic.talki.scoring.infrastructure.messaging.config.RabbitMQConfig;
import com.thropic.talki.scoring.infrastructure.messaging.producer.ScoringCompletedPublisher;
import com.thropic.talki.scoring.infrastructure.persistence.ScoreResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FillerAnalyzedConsumer {

    private static final Logger log = LoggerFactory.getLogger(FillerAnalyzedConsumer.class);

    private final ScoreCalculator scoreCalculator;
    private final ScoringCompletedPublisher publisher;
    private final ScoreResultRepository scoreResultRepository;

    public FillerAnalyzedConsumer(ScoreCalculator scoreCalculator,
                                   ScoringCompletedPublisher publisher,
                                   ScoreResultRepository scoreResultRepository) {
        this.scoreCalculator = scoreCalculator;
        this.publisher = publisher;
        this.scoreResultRepository = scoreResultRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    @Transactional
    public void handle(FillerAnalyzedEvent event) {
        log.info("[scoring-service] Received fillers.analyzed — sessionId={} totalFillers={}",
                event.getSessionId(), event.getTotalFillers());

        // Idempotencia: si ya existe un ScoreResult para esta sesión, no recalculamos.
        if (scoreResultRepository.findBySessionId(event.getSessionId()).isPresent()) {
            log.warn("[scoring-service] Duplicate fillers.analyzed ignored — sessionId={}",
                    event.getSessionId());
            return;
        }

        ScoringCompletedEvent scoring = scoreCalculator.calculate(event);
        VoiceScore scores = scoring.getScores();

        ScoreResult persisted = new ScoreResult(
                event.getSessionId(),
                event.getUserId(),
                scores,
                event.getTotalFillers(),
                scoring.getWordsPerMinute(),
                scoring.getSilenceRatio()
        );
        scoreResultRepository.save(persisted);

        log.info("[scoring-service] Score persisted and published — sessionId={} overall={}",
                event.getSessionId(), scoring.getOverallScore());

        publisher.publish(scoring);
    }
}
