package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.actividad.ActividadItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SuggestActivitiesPromptBuilder implements AbstractPromptBuilder {

    @Override
    public PromptTypeEnum getType() {
        return PromptTypeEnum.CHECKPOINT_SUGGEST;
    }

    private static final String SUGGEST_TEMPLATE = """
            <developer>
            userId: %s
            fecha: %s
            </developer>

            <actividades>
            %s
            </actividades>
            """;

    public String buildSuggestionPrompt(String userId, LocalDate fecha, List<ActividadItem> actividades) {
        return SUGGEST_TEMPLATE.formatted(userId, fecha, actividades);
    }
}
