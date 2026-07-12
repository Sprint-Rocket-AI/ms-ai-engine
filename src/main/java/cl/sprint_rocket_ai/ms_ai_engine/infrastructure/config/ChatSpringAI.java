package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.context_filter.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class ChatSpringAI {
    private static final Logger log = LoggerFactory.getLogger(ChatSpringAI.class);
    private final ChatClient chatClientWithMemory;
    private final ChatClient chatClientStateless;

    public ChatSpringAI(ChatClient.Builder chatClientBuilder,
                        ChatMemory chatMemory,
                        SyncMcpToolCallbackProvider provider) {

        ToolCallback[] tools = provider.getToolCallbacks();

        this.chatClientWithMemory = chatClientBuilder.build()
                .mutate()
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(tools)
                .build();

        this.chatClientStateless = chatClientBuilder.build()
                .mutate()
                .defaultTools(tools)
                .build();
    }

    public String generate(String sessionId, String systemPrompt, String userPrompt) {
        log.info("Generando respuesta con memoria para sessionId: {}", sessionId);
        return chatClientWithMemory.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .toolContext(buildToolContext())
                .call()
                .content();
    }

    public String generate(String systemPrompt, String userPrompt) {
        log.info("Generando respuesta sin memoria");
        return chatClientStateless.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .toolContext(buildToolContext())
                .call()
                .content();
    }

    private Map<String, Object> buildToolContext() {
        String userId = UserContextHolder.getUserId();
        return (userId != null)
                ? Map.of("userId", userId)
                : Map.of();
    }


}
