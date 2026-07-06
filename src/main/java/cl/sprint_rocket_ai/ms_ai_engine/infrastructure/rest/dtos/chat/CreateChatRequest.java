package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat;

import jakarta.validation.constraints.NotBlank;

public record CreateChatRequest(
        @NotBlank(message = "El userId no puede estar vacío")
        String userId,
        @NotBlank(message = "El title no puede estar vacío")
        String title
) {
}
