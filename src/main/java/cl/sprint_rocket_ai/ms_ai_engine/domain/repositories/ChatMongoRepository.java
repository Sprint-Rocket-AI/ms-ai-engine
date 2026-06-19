package cl.sprint_rocket_ai.ms_ai_engine.domain.repositories;

import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMongoRepository extends MongoRepository<Chat, String> {
    List<Chat> findByUserIdOrderByCreatedAtDesc(String userId);
    long deleteBySessionId(String sessionId);
}

