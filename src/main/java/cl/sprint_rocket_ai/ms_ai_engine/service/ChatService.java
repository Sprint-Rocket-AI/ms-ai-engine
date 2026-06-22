package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.Chat;
import cl.sprint_rocket_ai.ms_ai_engine.domain.repositories.ChatMongoRepository;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.ChatResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.CreateChatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final ChatMongoRepository chatMongoRepository;

    public ChatService(ChatMongoRepository chatMongoRepository) {
        this.chatMongoRepository = chatMongoRepository;
    }

    public String createChat(CreateChatRequest request) {
        log.info("Creando nuevo chat para userId: {}", request.userId());
        Chat chat = new Chat();
        chat.setSessionId(UUID.randomUUID().toString());
        Instant now = Instant.now();
        chat.setCreatedAt(now);
        chat.setUserId(request.userId());
        chat.setTitle(request.title());
        chatMongoRepository.save(chat);
        log.info("Chat creado para userId: {} y sessionId: {}", request.userId(), chat.getSessionId());
        return chat.getSessionId();
    }

    public List<ChatResponse> getChatsByUserId(String userId) {
        log.info("Obteniendo chats para userId: {}", userId);
        List<Chat> chats = chatMongoRepository.findByUserIdOrderByCreatedAtDesc(userId);
        log.info("Se encontraron {} chats para userId: {}", chats.size(), userId);
        return chats
                .stream()
                .map(ChatResponse::from)
                .toList();
    }

    public long deleteChatBySessionId(String sessionId) {
        log.info("Eliminando chat para sessionId: {}", sessionId);
        long deletedCount = chatMongoRepository.deleteBySessionId(sessionId);
        log.info("Se eliminaron {} chats para sessionId: {}", deletedCount, sessionId);
        return deletedCount;
    }

}

