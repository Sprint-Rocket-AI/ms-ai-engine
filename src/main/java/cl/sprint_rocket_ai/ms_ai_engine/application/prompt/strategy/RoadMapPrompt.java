package cl.sprint_rocket_ai.ms_ai_engine.application.prompt.strategy;

import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptType;
import org.springframework.stereotype.Component;

@Component
public class RoadMapPrompt implements PromptBuilder {

    private static final String SYSTEM_PROMPT = """
            Eres un planificador de rutas de aprendizaje. Generas
            roadmaps estructurados por etapas y prioridades. Organiza
            el contenido de forma progresiva: desde fundamentos hasta
            nivel avanzado, con hitos claros y recursos recomendados.
            """;

    @Override
    public PromptType getType() {
        return PromptType.ROADMAP;
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
}