package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;


@Component
public class ChatSpringAI {
    private static final Logger log = LoggerFactory.getLogger(ChatSpringAI.class);
    private final ChatClient chatClient;

    public ChatSpringAI(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generate(String sessionId,String systemPrompt, String userPrompt) {
        log.info("Generando respuesta desde AI para la sessionId: {}",sessionId);
        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .advisors(a -> a.param("sessionId", sessionId))
                .call()
                .content();
        log.info("Respuesta generada para la sessionId: {}",sessionId);
        return response;
    }

}
