package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecordatorioPromptBuilder {
    private static final String TEMPLATE = """
        <context>
        fechaHoraActual: %s
        userId:%s
        </context>
        
        <question>
        %s
        </question>
        """;

    public String build(String userPrompt, String userId, LocalDateTime fechaHoraActual) {
        return TEMPLATE.formatted(fechaHoraActual, userId, userPrompt);
    }
}
