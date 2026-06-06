package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.rag;

import jakarta.validation.constraints.NotBlank;


public record RAGRequest(
        @NotBlank String query
) {
}

