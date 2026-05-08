package com.thropic.talki.transcription.infrastructure.messaging.consumer;

import com.thropic.talki.transcription.domain.event.SessionLiveFinalizedEvent;
import com.thropic.talki.transcription.domain.event.TranscriptionCompletedEvent;
import com.thropic.talki.transcription.infrastructure.messaging.config.RabbitMQConfig;
import com.thropic.talki.transcription.infrastructure.messaging.producer.TranscriptionCompletedPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SessionFinalizedConsumer {

    private static final Logger log = LoggerFactory.getLogger(SessionFinalizedConsumer.class);

    private final TranscriptionCompletedPublisher publisher;

    public SessionFinalizedConsumer(TranscriptionCompletedPublisher publisher) {
        this.publisher = publisher;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handle(SessionLiveFinalizedEvent event) {
        log.info("[transcription-service] Received session.live.finalized — sessionId={} audioUri={}",
                event.getSessionId(), event.getAudioUri());

        // Simulación: en producción llamaría a Whisper STT via OpenAI API
        String transcriptionText = simulateWhisperTranscription(event);
        int wordCount = transcriptionText.split("\\s+").length;
        int wordsPerMinute = event.getDurationSeconds() > 0
                ? (wordCount * 60) / event.getDurationSeconds()
                : 0;

        log.info("[transcription-service] Transcription completed — sessionId={} words={} wpm={}",
                event.getSessionId(), wordCount, wordsPerMinute);

        TranscriptionCompletedEvent completed = new TranscriptionCompletedEvent(
                event.getSessionId(),
                event.getUserId(),
                transcriptionText,
                wordCount,
                event.getDurationSeconds(),
                event.getTraceId()
        );

        publisher.publish(completed);
    }

    private String simulateWhisperTranscription(SessionLiveFinalizedEvent event) {
        return "Buenos días, mi nombre es estudiante y hoy les voy a presentar este proyecto. " +
               "O sea, básicamente lo que queremos lograr es mejorar la comunicación oral. " +
               "Eh, como pueden ver en la diapositiva, tenemos tres objetivos principales. " +
               "Este es el primero y básicamente consiste en practicar de forma regular.";
    }
}
