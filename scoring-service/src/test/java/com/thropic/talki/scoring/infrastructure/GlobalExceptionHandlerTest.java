package com.thropic.talki.scoring.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleBadRequest_shouldReturn400WithInvalidArgumentCode() {
        IllegalArgumentException ex = new IllegalArgumentException("session_id missing");

        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("code", "INVALID_ARGUMENT");
        assertThat(response.getBody()).containsEntry("message", "session_id missing");
        assertThat(response.getBody()).containsKey("timestamp");
    }

    @Test
    void handleConflict_whenIllegalState_shouldReturn409WithInvalidStateCode() {
        IllegalStateException ex = new IllegalStateException("Session must be RECORDING to finalize");

        ResponseEntity<Map<String, Object>> response = handler.handleConflict(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsEntry("code", "INVALID_STATE");
        assertThat(response.getBody()).containsEntry("message", "Session must be RECORDING to finalize");
    }

    @Test
    void handleDataIntegrity_shouldReturn409WithDuplicateResourceCode() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
                "duplicate key violates unique constraint score_results_session_id_key",
                new RuntimeException("PSQLException: duplicate key"));

        ResponseEntity<Map<String, Object>> response = handler.handleDataIntegrity(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).containsEntry("code", "DUPLICATE_RESOURCE");
        assertThat(response.getBody()).containsEntry("message", "Resource already exists");
    }

    @Test
    void handleUnexpected_shouldReturn500WithInternalErrorCode() {
        Exception ex = new RuntimeException("NullPointerException somewhere");

        ResponseEntity<Map<String, Object>> response = handler.handleUnexpected(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).containsEntry("code", "INTERNAL_ERROR");
        assertThat(response.getBody()).containsEntry("message", "Unexpected error occurred");
    }

    @Test
    void allHandlers_shouldIncludeTimestampInIsoFormat() {
        ResponseEntity<Map<String, Object>> bad = handler.handleBadRequest(new IllegalArgumentException("x"));
        ResponseEntity<Map<String, Object>> conflict = handler.handleConflict(new IllegalStateException("x"));
        ResponseEntity<Map<String, Object>> unexpected = handler.handleUnexpected(new Exception("x"));

        for (ResponseEntity<Map<String, Object>> r : new ResponseEntity[]{bad, conflict, unexpected}) {
            String ts = (String) r.getBody().get("timestamp");
            assertThat(ts).matches("\\d{4}-\\d{2}-\\d{2}T.*Z");
        }
    }
}
