package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.builders.DefaultPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.AIRequest;
import cl.sprint_rocket_ai.ms_ai_engine.domain.model.VectorDocument;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.VectorStorePortOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public RAGService(LLMPortOut llmPortOut, VectorStorePortOut vectorStorePortOut, DefaultPromptBuilder promptBuilder, SystemPromptLoaderUtils loaderUtils) {
        this.llmPortOut = llmPortOut;
        this.vectorStorePortOut = vectorStorePortOut;
        this.promptBuilder = promptBuilder;
        this.loaderUtils = loaderUtils;
    }

    public String ask(AIRequest request) {
        String userPrompt = request.userPrompt();
        log.info("Inicio RAG userPrompt='{}'", userPrompt);
        List<VectorDocument> docs = vectorStorePortOut.search(request.userPrompt());
        log.info("Generando el contexto");
        String context = this.getContext(docs);
        log.info("Contexto generado, creando Prompt");
        String prompt = promptBuilder.buildWithContext( userPrompt, context);
        log.info("Prompt creado, cargando systemPrompt");
        String systemPrompt = loaderUtils.load(RAG.getPath());
        String answer = llmPortOut.generate(systemPrompt,prompt);
        log.info("Fin RAG docs={}", docs.size());
        return answer;
    }

    private String getContext(List<VectorDocument> docs){
        return docs.stream()
                .map(VectorDocument::content)
                .limit(5)
                .collect(Collectors.joining("\n"));
    }
}
