package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.chat_memory;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.persistences.mongodb.ChatMessageMongoRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatMemory chatMemory(ChatMessageMongoRepository messageRepo) {
        return new MongoChatMemory(messageRepo);
    }
}