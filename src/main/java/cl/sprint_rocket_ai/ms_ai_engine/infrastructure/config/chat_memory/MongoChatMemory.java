package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.chat_memory;

import cl.sprint_rocket_ai.ms_ai_engine.domain.model.ChatMessage;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.persistences.mongodb.ChatMessageMongoRepository;
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
    public void add(String sesionId, List<Message> messages) {

        List<ChatMessage> entities = messages.stream()
                .map(msg -> {
                    ChatMessage entity = new ChatMessage();
                    entity.setSessionId(sesionId);
                    entity.setRole(msg.getMessageType().name());
                    entity.setContent(msg.getText());
                    entity.setTimestamp(Instant.now());
                    return entity;
                })
                .toList();

        repository.saveAll(entities);
    }

    @Override
    public List<Message> get(String sesionId) {

        List<ChatMessage> entities = repository.findTop10BySessionIdOrderByTimestampDesc(sesionId);

        Collections.reverse(entities);

        List<Message> result = new ArrayList<>();

        for (ChatMessage m : entities) {
            switch (m.getRole()) {
                case "USER" -> result.add(new UserMessage(m.getContent()));
                case "ASSISTANT" -> result.add(new AssistantMessage(m.getContent()));
                case "SYSTEM" -> result.add(new SystemMessage(m.getContent()));
                default -> {
                    // fallback defensivo
                    result.add(new UserMessage(m.getContent()));
                }
            }
        }

        return result;
    }

    @Override
    public void clear(String sesionId) {
        // opcional: podrías implementar deleteBySessionId
    }
}