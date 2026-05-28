package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGRequest;
import cl.sprint_rocket_ai.ms_ai_engine.domain.model.VectorDocument;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.VectorStorePortOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RAGService {
    private static final Logger log = LoggerFactory.getLogger(RAGService.class);
    private final LLMPortOut llmPortOut;
    private final VectorStorePortOut vectorStorePortOut;

    public RAGService(LLMPortOut llmPortOut, VectorStorePortOut vectorStorePortOut) {
        this.llmPortOut = llmPortOut;
        this.vectorStorePortOut = vectorStorePortOut;
    }

    public String ask(RAGRequest request) {
        log.info("Inicio RAG query='{}'", request.query());
        List<VectorDocument> docs = vectorStorePortOut.search(request.query());
        String context = buildContext(docs);
        String prompt = buildPrompt(request, context);
        String answer = llmPortOut.generate(prompt);
        log.info("Fin RAG docs={} answerLength={}", docs.size(), answer == null ? 0 : answer.length());
        return answer;
    }

    private String buildContext(List<VectorDocument> docs) {
        return docs.stream()
                .map(VectorDocument::content)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n---\n"));
    }

    private String buildPrompt(RAGRequest request, String context) {
        String base = request.promptBase();
        String header = base == null || base.isBlank() ? "Context:" : base.trim() + "\n\nContext:";
        return header + "\n" + context + "\n\nQuestion:\n" + request.query() + "\n\nAnswer:";
    }
}
