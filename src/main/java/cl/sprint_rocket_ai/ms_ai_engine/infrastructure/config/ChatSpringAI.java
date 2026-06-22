package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ChatSpringAI {
    private static final Logger log = LoggerFactory.getLogger(ChatSpringAI.class);
    private final ChatClient chatClient;

    public ChatSpringAI(ChatClient.Builder chatClientBuilder,
                        List<ToolCallbackProvider> providers) {

        System.out.println("Providers encontrados: " + providers.size());
        this.chatClient = chatClientBuilder
                .defaultTools(providers.toArray(new ToolCallbackProvider[0]))
                .build();
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
