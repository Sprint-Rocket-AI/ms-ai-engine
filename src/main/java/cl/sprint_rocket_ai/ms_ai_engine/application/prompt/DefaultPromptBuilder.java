package cl.sprint_rocket_ai.ms_ai_engine.application.prompt;

import cl.sprint_rocket_ai.ms_ai_engine.domain.model.VectorDocument;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultPromptBuilder implements PromptBuilder{
    @Override
    public String build(RAGRequest request, List<VectorDocument> docs) {
        String context = docs.stream()
                .map(VectorDocument::content)
                .limit(5)
                .collect(Collectors.joining("\n---\n"));

        return """
            Eres un asistente. Usa el contexto para responder en español.

            Contexto:
            %s

            Pregunta:
            %s

            """.formatted(context, request.query());
    }
}
