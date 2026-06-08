package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.out;

import cl.sprint_rocket_ai.ms_ai_engine.domain.model.ChatMessage;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.ChatMessagePortOut;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.persistences.mongodb.ChatMessageMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMessageAdapterOut implements ChatMessagePortOut {
    private static final Logger log = LoggerFactory.getLogger(ChatMessageAdapterOut.class);
    private final ChatMessageMongoRepository mongoRepository;

    public ChatMessageAdapterOut(ChatMessageMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public List<ChatMessage> findTop10BySessionIdOrderByTimestampDesc(String sessionId) {
        log.info("Buscando últimos chats de la sesión: {}, desde MongoDB",sessionId);
        List<ChatMessage> result = mongoRepository.findTop10BySessionIdOrderByTimestampDesc(sessionId);
        log.info("Cantidad historial: {}",result.size());
        return result;
    }

    @Override
    public List<ChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId) {
        log.info("Buscando historial completo de la sesión: {}",sessionId);
        List<ChatMessage> result = mongoRepository.findTop10BySessionIdOrderByTimestampDesc(sessionId);
        log.info("Cantidad historial completo: {}",result.size());
        return result;
    }
}
