package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.ChatMessage;
import cl.sprint_rocket_ai.ms_ai_engine.domain.repositories.ChatMessageMongoRepository;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.ChatMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    private static final Logger log = LoggerFactory.getLogger(ChatMessageService.class);
    private final ChatMessageMongoRepository messageRepository;

    public ChatMessageService(ChatMessageMongoRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<ChatMessageResponse> getFullHistory(String sessionId) {
        log.info("Obteniendo todos los mensajes del chat sessionId: {}",sessionId);
        List<ChatMessage> messages = messageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
        log.info("Cantidad de mensajes obtenidos: {} para la sessionId: {}",messages.size(),sessionId);
        return messages
                .stream()
                .map(ChatMessageResponse::from)
                .toList();
    }

    public long deleteMessagesBySessionId(String sessionId) {
        log.info("Eliminando mensajes para sessionId: {}", sessionId);
        long deletedCount = messageRepository.deleteBySessionId(sessionId);
        log.info("Se eliminaron {} mensajes para sessionId: {}", deletedCount, sessionId);
        return deletedCount;
    }
}

