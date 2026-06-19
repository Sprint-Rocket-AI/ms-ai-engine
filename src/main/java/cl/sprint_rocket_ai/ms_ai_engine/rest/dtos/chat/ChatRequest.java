package cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.chat;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(
        @NotBlank(message = "El userId no puede estar vacío")
        String userId
) {
}
