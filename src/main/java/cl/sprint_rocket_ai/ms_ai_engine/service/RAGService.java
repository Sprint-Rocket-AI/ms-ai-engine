package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.RAGPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.semantic_cache.SemanticCacheAdvisor;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.AIRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RAGService {

    private static final Logger log = LoggerFactory.getLogger(RAGService.class);

    private final ChatSpringAI chatSpringAI;
    private final VectorStoreService vectorStoreService;
    private final RAGPromptBuilder promptBuilder;
    private final SystemPromptLoaderUtils loaderUtils;
    private final SemanticCacheAdvisor semanticCache;

    public RAGService(ChatSpringAI chatSpringAI,
                      VectorStoreService vectorStoreService,
                      RAGPromptBuilder promptBuilder,
                      SystemPromptLoaderUtils loaderUtils,
                      SemanticCacheAdvisor semanticCache) {
        this.chatSpringAI = chatSpringAI;
        this.vectorStoreService = vectorStoreService;
        this.promptBuilder = promptBuilder;
        this.loaderUtils = loaderUtils;
        this.semanticCache = semanticCache;
    }

    public String ask(AIRequest request) {
        log.info("Inicio RAG userPrompt='{}', sessionId: {}", request.userPrompt(), request.sessionId());
        return semanticCache.findInCache(request.userPrompt())
                .orElseGet(() -> generateAndSaveCache(request));
    }

    private String generateAndSaveCache(AIRequest request) {
        String answer = generateAnswer(request);
        semanticCache.saveToCache(request.userPrompt(), answer);
        return answer;
    }

    private String generateAnswer(AIRequest request) {
        String context = getContext(vectorStoreService.search(request.userPrompt()));
        boolean hasContext = !context.isEmpty();
        log.info("Contruyendo prompt...");

        String prompt = hasContext
                ?promptBuilder.buildWithContext(request.userPrompt(), context)
                :promptBuilder.build(request.userPrompt());

        String systemPrompt = loaderUtils.load(promptBuilder.getType().getPathSystemPrompt());
        log.info("Fin de la construcción");
        return chatSpringAI.generate(request.sessionId(), systemPrompt, prompt);
    }

    private String getContext(List<Document> docs) {
        return docs.stream()
                .limit(5)
                .map(Document::getText)
                .collect(Collectors.joining("\n"));
    }

}