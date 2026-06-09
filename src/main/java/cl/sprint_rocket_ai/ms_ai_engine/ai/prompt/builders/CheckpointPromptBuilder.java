package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.ActividadItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CheckpointPromptBuilder {

    private static final String SUGGEST_TEMPLATE = """
            <developer>
            userId: %s
            fecha: %s
            </developer>

            <actividades>
            %s
            </actividades>
            """;

    private static final String SUMMARY_TEMPLATE = """
            <developer>
            userId: %s
            fecha: %s
            </developer>

            <actividades>
            %s
            </actividades>
            """;

    public String buildSuggestionPrompt(String userId, LocalDate fecha, List<ActividadItem> actividades) {
        return SUGGEST_TEMPLATE.formatted(userId, fecha, buildActividadesContext(actividades));
    }

    public String buildSummaryPrompt(String userId, LocalDate fecha, List<ActividadItem> actividades) {
        return SUMMARY_TEMPLATE.formatted(userId, fecha, buildActividadesContext(actividades));
    }

    private String buildActividadesContext(List<ActividadItem> actividades) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < actividades.size(); i++) {
            ActividadItem a = actividades.get(i);
            sb.append(i + 1).append(". [").append(a.estado()).append("] ").append(a.titulo());
            if (a.prioridad() != null)    sb.append(" | Prioridad: ").append(a.prioridad());
            if (a.ticketJira() != null)   sb.append(" | Jira: ").append(a.ticketJira());
            if (a.descripcion() != null)  sb.append("\n   Descripción: ").append(a.descripcion());
            if (a.notas() != null)        sb.append("\n   Notas: ").append(a.notas());
            if (a.horasReales() != null)  sb.append("\n   Horas reales: ").append(a.horasReales());
            sb.append("\n");
        }
        return sb.toString();
    }
}
