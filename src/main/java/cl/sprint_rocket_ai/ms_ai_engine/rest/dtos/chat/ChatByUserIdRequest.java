package cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.chat;

import jakarta.validation.constraints.NotBlank;

public record ChatByUserIdRequest(
        @NotBlank(message = "El userId no puede estar vacío")
        String userId
) {
}
