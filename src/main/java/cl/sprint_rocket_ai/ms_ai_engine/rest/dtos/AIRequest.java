package cl.sprint_rocket_ai.ms_ai_engine.rest.dtos;

import jakarta.validation.constraints.NotBlank;


public record AIRequest(
        @NotBlank String sessionId,
        @NotBlank String userPrompt
) {
}

