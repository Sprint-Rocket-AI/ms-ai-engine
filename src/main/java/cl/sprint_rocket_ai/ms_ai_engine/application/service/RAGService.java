package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptType;
import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.factory.PromptFactory;
import cl.sprint_rocket_ai.ms_ai_engine.domain.model.VectorDocument;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.VectorStorePortOut;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Orquesta el flujo RAG (Retrieval-Augmented Generation).
 *
 * <p>Flujo:
 * <ol>
 *   <li>Recibe el {@link RAGRequest} con el campo {@code module}.</li>
 *   <li>Resuelve el {@link PromptType} via {@link PromptType#fromString(String)}.</li>
 *   <li>Obtiene el {@link PromptBuilder} correcto desde {@link PromptFactory}.</li>
 *   <li>Recupera documentos relevantes del vector store.</li>
 *   <li>Construye el prompt y llama al LLM.</li>
 * </ol>
 */
@Service
public class RAGService {

    private static final Logger log = LoggerFactory.getLogger(RAGService.class);

    private final LLMPortOut llmPortOut;
    private final VectorStorePortOut vectorStorePortOut;
    private final PromptFactory promptFactory;

    public RAGService(LLMPortOut llmPortOut,
                      VectorStorePortOut vectorStorePortOut,
                      PromptFactory promptFactory) {
        this.llmPortOut = llmPortOut;
        this.vectorStorePortOut = vectorStorePortOut;
        this.promptFactory = promptFactory;
    }

    public String ask(RAGRequest request) {
        log.info("Inicio RAG | módulo='{}' query='{}'", request.module(), request.query());

        // 1. Resolver estrategia según módulo
        PromptType promptType = PromptType.fromString(request.module());
        PromptBuilder promptBuilder = promptFactory.getBuilder(promptType);
        log.info("Estrategia seleccionada: {}", promptType);

        // 2. Recuperar documentos del vector store
        List<VectorDocument> docs = vectorStorePortOut.search(request.query());
        log.info("Documentos recuperados: {}", docs.size());

        // 3. Convertir docs a contexto plano
        String context = docs.stream()
                .map(VectorDocument::content)
                .limit(5)
                .collect(Collectors.joining("\n---\n"));

        // 4. Construir prompt con la estrategia del módulo
        String prompt = promptBuilder.build(request.query(), context);

        // 5. Llamar al LLM y retornar respuesta
        String answer = llmPortOut.generate(prompt);
        log.info("Fin RAG | módulo='{}' docs={}", request.module(), docs.size());

        return answer;
    }
}
