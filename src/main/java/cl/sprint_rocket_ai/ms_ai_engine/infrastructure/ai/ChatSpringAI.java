package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;


@Component
public class ChatSpringAI {
    private final ChatClient chatClient;

    public ChatSpringAI(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generate(String sessionId,String systemPrompt, String userPrompt) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .advisors(a -> a.param("sessionId", sessionId))
                .call()
                .content();
    }

}
