package cl.sprint_rocket_ai.ms_ai_engine.application.prompt;

/**
 * Contrato del patrón Strategy para la construcción de prompts.
 * Cada módulo (devmentor, checkpoint, roadmap) tiene su propia implementación.
 */
public interface PromptBuilder {

    /**
     * Construye el prompt completo listo para enviar al LLM.
     *
     * @param userInput  la pregunta/input del usuario
     * @param context    el contexto recuperado del vector store (docs concatenados)
     * @return prompt completo como String
     */
    String build(String userInput, String context);

    /**
     * Identifica el tipo de módulo al que corresponde esta estrategia.
     * Usado por {@code PromptFactory} para registrar el builder.
     */
    PromptType getType();

    /**
     * Retorna el system prompt propio del módulo.
     */
    String buildSystemPrompt();
}
