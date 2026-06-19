package cl.sprint_rocket_ai.ms_ai_engine.domain.repositories;

import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findTop10BySessionIdOrderByTimestampDesc(String sessionId);
    List<ChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId);
}
