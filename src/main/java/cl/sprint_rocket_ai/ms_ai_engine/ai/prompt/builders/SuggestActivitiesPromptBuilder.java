package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.ActividadItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SuggestActivitiesPromptBuilder {

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
