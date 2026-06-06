package cl.sprint_rocket_ai.ms_ai_engine.application.prompt.strategy;

import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptType;
import org.springframework.stereotype.Component;

/**
 * Estrategia de prompt para el módulo Checkpoint.
 *
 * <p>Expone además dos métodos especializados para los flujos checkpoint:
 * <ul>
 *   <li>{@link #buildSuggestionPrompt(String, String)} — sugiere 5 actividades de continuidad.</li>
 *   <li>{@link #buildSummaryPrompt(String, String)} — genera resumen ejecutivo + sugerencias.</li>
 * </ul>
 *
 * <p>Los prompts especializados instruyen al LLM a responder en JSON para
 * que {@code CheckpointService} pueda deserializar la respuesta.
 */
@Component
public class CheckpointPrompt implements PromptBuilder {

    private static final String SYSTEM_PROMPT = """
            Eres un asistente de checkpoint. Tu tarea es evaluar
            el progreso del usuario y validar avances. Revisa si se
            cumplieron los objetivos planteados, señala los logros y
            sugiere los próximos pasos concretos.
            """;

    @Override
    public PromptType getType() {
        return PromptType.CHECKPOINT;
    }

    @Override
    public String buildSystemPrompt() {
        return SYSTEM_PROMPT;
    }

    @Override
    public String build(String userInput, String context) {
        return SYSTEM_PROMPT + "\n\nContexto:\n" + context
                + "\n\nUsuario:\n" + userInput;
    }

    /**
     * Construye el prompt para sugerir 5 actividades de continuidad.
     *
     * <p>Instruye al LLM a responder EXCLUSIVAMENTE con JSON en el formato:
     * <pre>
     * {
     *   "sugerencias": [
     *     { "titulo": "...", "descripcion": "...", "prioridad": "ALTA|MEDIA|BAJA", "razon": "..." }
     *   ]
     * }
     * </pre>
     *
     * @param userId            identificador del desarrollador
     * @param actividadesContext resumen textual de las actividades del día
     * @return prompt completo listo para enviar al LLM
     */
    public String buildSuggestionPrompt(String userId, String actividadesContext) {
        return """
                Eres un asistente de checkpoint especializado en planificación de actividades de desarrollo de software.
                Analizarás las actividades realizadas por el desarrollador y sugerirás las 5 actividades más relevantes
                para dar continuidad y agregar valor al trabajo ya realizado.

                Desarrollador: %s

                Actividades del día:
                %s

                INSTRUCCIONES:
                - Analiza el estado de las actividades (completadas, pendientes, bloqueadas).
                - Identifica dependencias y flujos de trabajo incompletos.
                - Sugiere exactamente 5 actividades ordenadas por impacto descendente.
                - Las prioridades válidas son: ALTA, MEDIA, BAJA.
                - Responde ÚNICAMENTE con el siguiente JSON, sin texto adicional, sin bloques de código markdown:

                {
                  "sugerencias": [
                    {
                      "titulo": "Título concreto de la actividad",
                      "descripcion": "Descripción detallada de lo que debe hacerse",
                      "prioridad": "ALTA",
                      "razon": "Justificación basada en las actividades analizadas"
                    }
                  ]
                }
                """.formatted(userId, actividadesContext);
    }

    /**
     * Construye el prompt para generar el resumen ejecutivo diario + sugerencias.
     *
     * <p>Instruye al LLM a responder EXCLUSIVAMENTE con JSON en el formato:
     * <pre>
     * {
     *   "resumen": "...",
     *   "sugerencias": [
     *     { "titulo": "...", "descripcion": "...", "prioridad": "ALTA|MEDIA|BAJA", "razon": "..." }
     *   ]
     * }
     * </pre>
     *
     * @param userId            identificador del desarrollador
     * @param actividadesContext resumen textual de todas las actividades del día
     * @return prompt completo listo para enviar al LLM
     */
    public String buildSummaryPrompt(String userId, String actividadesContext) {
        return """
                Eres un asistente de checkpoint especializado en la generación de resúmenes ejecutivos de actividades
                de desarrollo de software.

                Desarrollador: %s

                Actividades del día (pendientes y completadas):
                %s

                INSTRUCCIONES:
                - Redacta un resumen ejecutivo conciso (máximo 5 párrafos) en español.
                - El resumen debe incluir: logros del día, actividades pendientes relevantes,
                  posibles bloqueadores identificados y observaciones generales.
                - Sugiere exactamente 5 actividades para el día siguiente que den continuidad al trabajo.
                - Las prioridades válidas son: ALTA, MEDIA, BAJA.
                - Responde ÚNICAMENTE con el siguiente JSON, sin texto adicional, sin bloques de código markdown:

                {
                  "resumen": "Texto del resumen ejecutivo...",
                  "sugerencias": [
                    {
                      "titulo": "Título concreto de la actividad",
                      "descripcion": "Descripción detallada de lo que debe hacerse",
                      "prioridad": "ALTA",
                      "razon": "Justificación basada en el trabajo del día"
                    }
                  ]
                }
                """.formatted(userId, actividadesContext);
    }
}
