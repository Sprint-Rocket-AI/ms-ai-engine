package cl.sprint_rocket_ai.ms_ai_engine.application.prompt.strategy;

import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptType;
import org.springframework.stereotype.Component;

/**
 * Estrategia de fallback: se usa cuando el módulo recibido en el request
 * no coincide con ninguno de los módulos registrados.
 *
 * <p><b>Nota:</b> Este builder <em>no</em> se registra en el {@code PromptFactory}
 * (ya que no existe un {@code PromptType} FALLBACK), sino que se invoca
 * explícitamente como respaldo en {@code RAGService}.
 */
@Component
public class DefaultPromptBuilder implements PromptBuilder {

    private static final String SYSTEM_PROMPT = """
            Eres un asistente inteligente. Usa el contexto proporcionado
            para responder la pregunta del usuario de forma clara y en español.
            """;

    @Override
    public PromptType getType() {
        // Este builder es fallback; no debe registrarse via PromptFactory
        return null;
    }

    @Override
    public String buildSystemPrompt() {
        return SYSTEM_PROMPT;
    }

    @Override
    public String build(String userInput, String context) {
        return """
            %s

            Contexto:
            %s

            Pregunta:
            %s
            """.formatted(SYSTEM_PROMPT, context, userInput);
    }
}
