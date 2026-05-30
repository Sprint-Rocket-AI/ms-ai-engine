package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGRequest;
import cl.sprint_rocket_ai.ms_ai_engine.domain.model.VectorDocument;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.VectorStorePortOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RAGService {
    private static final Logger log = LoggerFactory.getLogger(RAGService.class);
    private final LLMPortOut llmPortOut;
    private final VectorStorePortOut vectorStorePortOut;
    private final PromptBuilder promptBuilder;

    public RAGService(LLMPortOut llmPortOut, VectorStorePortOut vectorStorePortOut, PromptBuilder promptBuilder) {
        this.llmPortOut = llmPortOut;
        this.vectorStorePortOut = vectorStorePortOut;
        this.promptBuilder = promptBuilder;
    }

    public String ask(RAGRequest request) {
        log.info("Inicio RAG query='{}'", request.query());
        List<VectorDocument> docs = vectorStorePortOut.search(request.query());
        String prompt = promptBuilder.build(request, docs);
        String answer = llmPortOut.generate(prompt);
        log.info("Fin RAG docs={}", docs.size());
        return answer;
    }
}
