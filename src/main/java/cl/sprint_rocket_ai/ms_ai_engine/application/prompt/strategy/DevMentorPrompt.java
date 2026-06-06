package cl.sprint_rocket_ai.ms_ai_engine.application.prompt.strategy;

import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptType;
import org.springframework.stereotype.Component;

@Component
public class DevMentorPrompt implements PromptBuilder {

    private static final String SYSTEM_PROMPT = """
            Eres un mentor de desarrollo senior. Guías al usuario
            con buenas prácticas, code reviews y explicaciones técnicas.
            Fomenta el pensamiento crítico y justifica cada decisión
            de diseño con fundamentos sólidos.
            """;

    @Override
    public PromptType getType() {
        return PromptType.DEV_MENTOR;
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