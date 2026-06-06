package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.strategy.CheckpointPrompt;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.ActividadItem;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.ResumenDiarioRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.ResumenDiarioResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.SugerenciaItem;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.SugerirActividadesRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.SugerirActividadesResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Orquesta los dos flujos IA del módulo Checkpoint:
 * <ul>
 *   <li>{@link #suggest} — sugiere 5 actividades de continuidad (on-demand).</li>
 *   <li>{@link #dailySummary} — genera resumen ejecutivo diario + sugerencias (job 8:30).</li>
 * </ul>
 *
 * <p>Ambos flujos usan {@link CheckpointPrompt} con prompts que instruyen al LLM
 * a responder en JSON estructurado. El JSON se parsea con Jackson para construir
 * los DTOs de respuesta tipados.
 */
@Service
public class CheckpointService {

    private static final Logger log = LoggerFactory.getLogger(CheckpointService.class);

    private final LLMPortOut llmPortOut;
    private final CheckpointPrompt checkpointPrompt;
    private final ObjectMapper objectMapper;

    public CheckpointService(LLMPortOut llmPortOut,
                             CheckpointPrompt checkpointPrompt,
                             ObjectMapper objectMapper) {
        this.llmPortOut = llmPortOut;
        this.checkpointPrompt = checkpointPrompt;
        this.objectMapper = objectMapper;
    }

    /**
     * Sugiere 5 actividades de continuidad a partir de las actividades del día.
     *
     * @param request contiene userId, fecha y lista de actividades del día
     * @return respuesta con lista de 5 sugerencias
     */
    public SugerirActividadesResponse suggest(SugerirActividadesRequest request) {
        log.info("Iniciando sugerencia de actividades | userId='{}' fecha='{}'",
                request.userId(), request.fecha());

        String actividadesContext = buildActividadesContext(request.actividades());
        String prompt = checkpointPrompt.buildSuggestionPrompt(request.userId(), actividadesContext);

        log.info("Enviando {} actividades al LLM para sugerencias", request.actividades().size());
        String llmResponse = llmPortOut.generate(prompt);

        SugerirActividadesResponse response = parseSuggestionResponse(llmResponse, request.userId());
        log.info("Sugerencias generadas exitosamente | userId='{}' total={}",
                request.userId(), response.sugerencias().size());
        return response;
    }

    /**
     * Genera el resumen ejecutivo diario y sugerencias para el día siguiente.
     * Invocado por el scheduler de ms-checkpoint a las 8:30 a.m.
     *
     * @param request contiene userId, fecha y todas las actividades del día (pendientes + completadas)
     * @return respuesta con resumen ejecutivo y lista de sugerencias
     */
    public ResumenDiarioResponse dailySummary(ResumenDiarioRequest request) {
        log.info("Iniciando resumen diario | userId='{}' fecha='{}' actividades={}",
                request.userId(), request.fecha(), request.actividades().size());

        String actividadesContext = buildActividadesContext(request.actividades());
        String prompt = checkpointPrompt.buildSummaryPrompt(request.userId(), actividadesContext);

        log.info("Enviando actividades al LLM para resumen diario | userId='{}'", request.userId());
        String llmResponse = llmPortOut.generate(prompt);

        ResumenDiarioResponse response = parseDailySummaryResponse(llmResponse, request.userId());
        log.info("Resumen diario generado exitosamente | userId='{}' sugerencias={}",
                request.userId(), response.sugerencias().size());
        return response;
    }

    // ─── Helpers ────────────────────────────────────────────────────────────────

    /**
     * Convierte la lista de actividades en un bloque de texto estructurado para el prompt.
     */
    private String buildActividadesContext(List<ActividadItem> actividades) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < actividades.size(); i++) {
            ActividadItem a = actividades.get(i);
            sb.append(i + 1).append(". [").append(a.estado()).append("] ")
              .append(a.titulo());
            if (a.prioridad() != null) sb.append(" | Prioridad: ").append(a.prioridad());
            if (a.ticketJira() != null) sb.append(" | Jira: ").append(a.ticketJira());
            if (a.descripcion() != null) sb.append("\n   Descripción: ").append(a.descripcion());
            if (a.notas() != null) sb.append("\n   Notas: ").append(a.notas());
            if (a.horasReales() != null) sb.append("\n   Horas reales: ").append(a.horasReales());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Parsea la respuesta JSON del LLM para el endpoint de sugerencias.
     * En caso de error de parseo, genera una respuesta de fallback.
     */
    private SugerirActividadesResponse parseSuggestionResponse(String llmResponse, String userId) {
        try {
            String cleanJson = extractJson(llmResponse);
            JsonNode root = objectMapper.readTree(cleanJson);
            List<SugerenciaItem> sugerencias = objectMapper.convertValue(
                    root.get("sugerencias"),
                    new TypeReference<List<SugerenciaItem>>() {}
            );
            return new SugerirActividadesResponse(sugerencias);
        } catch (Exception e) {
            log.error("Error parseando respuesta de sugerencias del LLM para userId='{}': {}",
                    userId, e.getMessage());
            return new SugerirActividadesResponse(fallbackSugerencias());
        }
    }

    /**
     * Parsea la respuesta JSON del LLM para el endpoint de resumen diario.
     * En caso de error de parseo, genera una respuesta de fallback.
     */
    private ResumenDiarioResponse parseDailySummaryResponse(String llmResponse, String userId) {
        try {
            String cleanJson = extractJson(llmResponse);
            JsonNode root = objectMapper.readTree(cleanJson);
            String resumen = root.get("resumen").asText();
            List<SugerenciaItem> sugerencias = objectMapper.convertValue(
                    root.get("sugerencias"),
                    new TypeReference<List<SugerenciaItem>>() {}
            );
            return new ResumenDiarioResponse(resumen, sugerencias);
        } catch (Exception e) {
            log.error("Error parseando resumen diario del LLM para userId='{}': {}",
                    userId, e.getMessage());
            return new ResumenDiarioResponse(
                    "No fue posible generar el resumen ejecutivo automáticamente. Revisa el log para más detalles.",
                    fallbackSugerencias()
            );
        }
    }

    /**
     * Extrae el bloque JSON de la respuesta del LLM, ignorando texto circundante.
     * Busca el primer '{' y el último '}'.
     */
    private String extractJson(String llmResponse) {
        int start = llmResponse.indexOf('{');
        int end = llmResponse.lastIndexOf('}');
        if (start == -1 || end == -1 || start > end) {
            throw new IllegalArgumentException("La respuesta del LLM no contiene JSON válido");
        }
        return llmResponse.substring(start, end + 1);
    }

    /**
     * Sugerencias de fallback cuando el LLM no responde o el JSON es inválido.
     */
    private List<SugerenciaItem> fallbackSugerencias() {
        return List.of(
                new SugerenciaItem(
                        "Revisar actividades pendientes del día",
                        "Identificar y priorizar las tareas incompletas para continuar el flujo de trabajo",
                        "ALTA",
                        "Generado como fallback por error en la respuesta del LLM"
                )
        );
    }
}
