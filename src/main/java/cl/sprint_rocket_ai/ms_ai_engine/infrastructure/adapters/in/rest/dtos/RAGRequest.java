package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record RAGRequest(
        @NotBlank String query,
        @NotBlank String module,
        String promptBase,
        String collection,
        Map<String, Object> filters
) {
}
