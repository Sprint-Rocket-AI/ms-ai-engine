package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.llm;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeminisLLM {
    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;

    public GeminisLLM(ChatModel chatModel, EmbeddingModel embeddingModel) {
        this.chatClient = ChatClient.create(chatModel);
        this.embeddingModel = embeddingModel;
    }

    public String generate(String prompt) {
        return chatClient.prompt(prompt).call().content();
    }

    public List<float[]> embedding(List<String> texts) {
        return embeddingModel.embed(texts);
    }
}
