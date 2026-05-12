package com.thropic.talki.scoring.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3 para el scoring-service.
 *
 * Publica la especificación en /v3/api-docs y la UI en /swagger-ui.html.
 * Cumple el criterio "RESTful API con documentación de cada servicio" de la
 * rúbrica TP1 del curso SI657. El servicio implementa el bounded context
 * Speech Analysis (DDD) y calcula el Voice Coach Score (5 dimensiones:
 * fluency, clarity, volume, vocabulary, confidence) a partir del evento
 * fillers.analyzed con idempotencia at-least-once.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI scoringServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Talki — Scoring Service API")
                        .description("Microservicio del bounded context Speech Analysis (DDD) del producto "
                                + "Talki. Consume el evento fillers.analyzed del exchange talki.events, "
                                + "calcula el Voice Coach Score con 5 dimensiones (fluency, clarity, volume, "
                                + "vocabulary, confidence) cada una validada en el rango 0-100, y publica "
                                + "scoring.completed. Aplica idempotencia at-least-once mediante unique "
                                + "constraint sobre session_id + manejo de DataIntegrityViolationException.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Thropic — Equipo Talki")
                                .url("https://github.com/upc-pre-202601-si657-7940-thropic"))
                        .license(new License()
                                .name("Académico — UPC SI657 2026-10")))
                .servers(List.of(
                        new Server().url("http://localhost:8088").description("Local (perfil dev)"),
                        new Server().url("https://api.talki.app").description("Producción (Railway)")
                ));
    }
}
