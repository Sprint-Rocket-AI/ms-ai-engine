package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat;

import java.time.Instant;

public record CreateChatResponse(
        String sessionId,
        String title,
        String query,
        String answer,
        Instant createAt
) {
}
