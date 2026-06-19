package cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.chat;

import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.ChatMessage;
import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.Role;

import java.time.Instant;

public record ChatMessageResponse(
        Role role,
        String content,
        Instant timestamp
) {

    public static ChatMessageResponse from(ChatMessage chatMessage){
        return new ChatMessageResponse(
                chatMessage.getRole(),
                chatMessage.getContent(),
                chatMessage.getTimestamp()
        );
    }
}
