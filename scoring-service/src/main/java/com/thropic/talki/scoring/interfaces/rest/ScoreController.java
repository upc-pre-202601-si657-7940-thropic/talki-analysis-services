package com.thropic.talki.scoring.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/scores")
public class ScoreController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "service", "scoring-service",
                "status", "UP",
                "description", "Consumes fillers.analyzed → calcula VoiceScore → publica scoring.completed"
        ));
    }
}
