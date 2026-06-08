package cl.sprint_rocket_ai.ms_ai_engine.domain.port.out;

import cl.sprint_rocket_ai.ms_ai_engine.domain.model.ChatMessage;

import java.util.List;

public interface ChatMessagePortOut {
    List<ChatMessage> findTop10BySessionIdOrderByTimestampDesc(String sessionId);
    List<ChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId);
}
