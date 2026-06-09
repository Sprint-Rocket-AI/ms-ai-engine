package cl.sprint_rocket_ai.ms_ai_engine.repositories;

import cl.sprint_rocket_ai.ms_ai_engine.documents.ChatMessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocument, String> {
    List<ChatMessageDocument> findTop10BySessionIdOrderByTimestampDesc(String sessionId);
    List<ChatMessageDocument> findBySesionIdOrderByTimestampAsc(String sessionId);
}
