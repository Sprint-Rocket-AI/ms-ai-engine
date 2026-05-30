package cl.sprint_rocket_ai.ms_ai_engine.application.prompt;

import cl.sprint_rocket_ai.ms_ai_engine.domain.model.VectorDocument;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGRequest;

import java.util.List;

public interface PromptBuilder {
    String build(RAGRequest request, List<VectorDocument> docs);
}
