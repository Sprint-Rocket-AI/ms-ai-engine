package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecordatorioPromptBuilder implements AbstractPromptBuilder {

    @Override
    public PromptTypeEnum getType() {
        return PromptTypeEnum.RECORDATORIO_TOOL;
    }

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
