package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGRequest;
import cl.sprint_rocket_ai.ms_ai_engine.domain.model.VectorDocument;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.VectorStorePortOut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RAGService {
    private final LLMPortOut llmPortOut;
    private final VectorStorePortOut vectorStorePortOut;

    public RAGService(LLMPortOut llmPortOut, VectorStorePortOut vectorStorePortOut) {
        this.llmPortOut = llmPortOut;
        this.vectorStorePortOut = vectorStorePortOut;
    }

    public String ask(RAGRequest request) {
        List<VectorDocument> docs = vectorStorePortOut.search(request.query());
        String context = buildContext(docs);
        String prompt = buildPrompt(request, context);
        return llmPortOut.generate(prompt);
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
