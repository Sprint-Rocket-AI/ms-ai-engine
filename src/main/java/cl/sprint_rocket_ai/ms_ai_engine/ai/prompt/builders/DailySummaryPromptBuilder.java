package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.actividad.ActividadItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DailySummaryPromptBuilder {

    private static final String SUMMARY_TEMPLATE = """
            <developer>
            userId: %s
            fecha: %s
            </developer>

            <actividades>
            %s
            </actividades>
            """;

    public String buildSummaryPrompt(String userId, LocalDate fecha, List<ActividadItem> actividades) {
        return SUMMARY_TEMPLATE.formatted(userId, fecha, actividades);
    }
}

