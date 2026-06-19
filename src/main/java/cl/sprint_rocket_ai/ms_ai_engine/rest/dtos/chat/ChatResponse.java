package cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.chat;

import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.Chat;

import java.time.Instant;

public record ChatResponse(
      String sessionId,
      Instant createdAt,
      String title
) {
    public static ChatResponse from (Chat chat){
        return new ChatResponse(
                chat.getSessionId(),
                chat.getCreatedAt(),
                chat.getTitle()
        );
    }
}
