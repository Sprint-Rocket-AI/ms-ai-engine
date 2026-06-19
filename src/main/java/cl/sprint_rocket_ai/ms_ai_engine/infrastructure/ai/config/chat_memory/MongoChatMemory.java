package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai.config.chat_memory;

import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.ChatMessage;
import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.Role;
import cl.sprint_rocket_ai.ms_ai_engine.domain.repositories.ChatMessageMongoRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;

import java.time.Instant;
import java.util.*;

public class MongoChatMemory implements ChatMemory {

    private final ChatMessageMongoRepository repository;

    public MongoChatMemory(ChatMessageMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void add(@NonNull String sessionId, List<Message> messages) {

        List<ChatMessage> entities = messages.stream()
                .map(msg -> {
                    ChatMessage entity = new ChatMessage();
                    entity.setSessionId(sessionId);
                    entity.setRole(Role.valueOf(msg.getMessageType().name()));
                    entity.setContent(msg.getText());
                    entity.setTimestamp(Instant.now());
                    return entity;
                })
                .toList();

        repository.saveAll(entities);
    }

    @Override
    public List<Message> get(@NonNull String sesionId) {

        List<ChatMessage> entities = repository.findTop10BySessionIdOrderByTimestampDesc(sesionId);

        Collections.reverse(entities);

        List<Message> result = new ArrayList<>();

        for (ChatMessage m : entities) {
            switch (m.getRole()) {
                case USER -> result.add(new UserMessage(m.getContent()));
                case ASSISTANT -> result.add(new AssistantMessage(m.getContent()));
                case SYSTEM -> result.add(new SystemMessage(m.getContent()));
                default -> result.add(new UserMessage(m.getContent()));
            }
        }

        return result;
    }

    @Override
    public void clear(@NonNull String sessionId) {
        // opcional: podrías implementar deleteBySessionId
    }
}