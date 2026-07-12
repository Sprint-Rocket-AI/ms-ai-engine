package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.chat_memory.MongoChatMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ChatSpringAI {
    private static final Logger log = LoggerFactory.getLogger(ChatSpringAI.class);
    private final ChatClient chatClient;

    public ChatSpringAI(ChatClient.Builder chatClientBuilder,
                        ChatMemory chatMemory,
                        SyncMcpToolCallbackProvider provider
    ) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultTools((ToolCallback[]) provider.getToolCallbacks())
                .build();
    }

    public String generate(String sessionId, String systemPrompt, String userPrompt) {
        log.info("Generando respuesta desde AI para la sessionId: {}", sessionId);
        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .content();

        log.info("Respuesta generada para la sessionId: {}", sessionId);
        return response;
    }

    public String generate(String systemPrompt, String userPrompt) {
        log.info("Generando respuesta desde AI");
        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, MongoChatMemory.NO_MEMORY_CONVERSATION_ID))
                .call()
                .content();
        log.info("Respuesta generada");
        return response;
    }
}
