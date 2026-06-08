package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.builders.DefaultPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.semantic_cache.SemanticCacheAdvisor;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.AIRequest;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.VectorStorePortOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.SystemPromptTypeEnum.RAG;

@Service
public class RAGService {

    private static final Logger log = LoggerFactory.getLogger(RAGService.class);

    private final LLMPortOut llmPortOut;
    private final VectorStorePortOut vectorStorePortOut;
    private final DefaultPromptBuilder promptBuilder;
    private final SystemPromptLoaderUtils loaderUtils;
    private final SemanticCacheAdvisor semanticCache;

    public RAGService(LLMPortOut llmPortOut,
                      VectorStorePortOut vectorStorePortOut,
                      DefaultPromptBuilder promptBuilder,
                      SystemPromptLoaderUtils loaderUtils,
                      SemanticCacheAdvisor semanticCache) {
        this.llmPortOut = llmPortOut;
        this.vectorStorePortOut = vectorStorePortOut;
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
        String context = getContext(vectorStorePortOut.search(request.userPrompt()));
        String prompt = promptBuilder.buildWithContext(request.userPrompt(), context);
        String systemPrompt = loaderUtils.load(RAG.getPath());
        return llmPortOut.generate(request.sessionId(), systemPrompt, prompt);
    }

    private String getContext(List<Document> docs) {
        return docs.stream()
                .limit(5)
                .map(Document::getText)
                .collect(Collectors.joining("\n"));
    }
}