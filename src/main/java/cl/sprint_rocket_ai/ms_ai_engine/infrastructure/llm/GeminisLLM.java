package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;


@Component
public class GeminisLLM {
    private final ChatClient chatClient;

    public GeminisLLM(ChatClient.Builder chatClientBuilder) {
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
