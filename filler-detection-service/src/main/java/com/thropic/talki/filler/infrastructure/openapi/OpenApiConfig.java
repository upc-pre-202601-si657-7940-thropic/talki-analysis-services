package com.thropic.talki.filler.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3 para el filler-detection-service.
 *
 * Publica la especificación en /v3/api-docs y la UI en /swagger-ui.html.
 * Cumple el criterio "RESTful API con documentación de cada servicio" de la
 * rúbrica TP1 del curso SI657. El servicio consume session.live.finalized
 * con transcript_gemini y métricas acústicas, detecta muletillas en el
 * texto y publica fillers.analyzed extendiendo las métricas aguas abajo.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fillerDetectionServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Talki — Filler Detection Service API")
                        .description("Microservicio del bounded context Speech Analysis (DDD) del producto "
                                + "Talki. Consume session.live.finalized del exchange talki.events con el "
                                + "transcript_gemini y las métricas acústicas (words_per_minute, "
                                + "silence_ratio, volume_rms_avg) ya calculadas por live-coach-service. "
                                + "Aplica detección de muletillas y publica fillers.analyzed propagando "
                                + "todas las métricas al scoring-service downstream.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Thropic — Equipo Talki")
                                .url("https://github.com/upc-pre-202601-si657-7940-thropic"))
                        .license(new License()
                                .name("Académico — UPC SI657 2026-10")))
                .servers(List.of(
                        new Server().url("http://localhost:8087").description("Local (perfil dev)"),
                        new Server().url("https://api.talki.app").description("Producción (Railway)")
                ));
    }
}
