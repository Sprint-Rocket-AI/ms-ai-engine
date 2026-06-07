package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeminisLLM {
    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;

    public GeminisLLM(ChatClient.Builder chatClientBuilder, EmbeddingModel embeddingModel) {
        this.chatClient = chatClientBuilder.build();
        this.embeddingModel = embeddingModel;
    }

    public String generate(String systemPrompt, String userPrompt) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }

    public List<float[]> embedding(List<String> texts) {
        return embeddingModel.embed(texts);
    }
}
