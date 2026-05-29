package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record AIIndexRequest(
        @NotBlank String id,
        @NotBlank String tipo,
        @NotBlank String contenido,
        List<String> tags,
        @NotNull Map<String, Object> metadata
) {
}

