package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.CheckpointPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.ResumenDiarioRequest;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.ResumenDiarioResponse;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.SugerenciaItem;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.SugerirActividadesRequest;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.SugerirActividadesResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.SystemPromptTypeEnum.CHECKPOINT_DAILY_SUMMARY;
import static cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.SystemPromptTypeEnum.CHECKPOINT_SUGGEST;

@Service
public class CheckpointService {

    private static final Logger log = LoggerFactory.getLogger(CheckpointService.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final CheckpointPromptBuilder promptBuilder;
    private final ChatSpringAI chatSpringAI;
    private final ObjectMapper objectMapper;

    public CheckpointService(SystemPromptLoaderUtils loaderUtils,
                             CheckpointPromptBuilder promptBuilder,
                             ChatSpringAI chatSpringAI) {
        this.loaderUtils = loaderUtils;
        this.promptBuilder = promptBuilder;
        this.chatSpringAI = chatSpringAI;
        this.objectMapper = new ObjectMapper();
    }

    public SugerirActividadesResponse suggest(SugerirActividadesRequest request) {
        log.info("Iniciando sugerencia de actividades | userId='{}' fecha='{}'",
                request.userId(), request.fecha());
        try {
            String prompt = promptBuilder.buildSuggestionPrompt(request.userId(), request.fecha(), request.actividades());
            log.info("Prompt creado, cargando systemPrompt");
            String systemPrompt = loaderUtils.load(CHECKPOINT_SUGGEST.getPath());
            log.info("SystemPrompt cargado correctamente");

            String llmResponse = chatSpringAI.generate("",systemPrompt, prompt);

            SugerirActividadesResponse response = parseSuggestionResponse(llmResponse, request.userId());
            log.info("Sugerencias generadas exitosamente | userId='{}' total={}",
                    request.userId(), response.sugerencias().size());
            return response;
        } catch (Exception e) {
            log.error("Error generando sugerencias para userId='{}': {}", request.userId(), e.getMessage(), e);
            return new SugerirActividadesResponse(fallbackSugerencias());
        }
    }

    public ResumenDiarioResponse dailySummary(ResumenDiarioRequest request) {
        log.info("Iniciando resumen diario | userId='{}' fecha='{}' actividades={}",
                request.userId(), request.fecha(), request.actividades().size());
        try {
            String prompt = promptBuilder.buildSummaryPrompt(request.userId(), request.fecha(), request.actividades());
            log.info("Prompt creado, cargando systemPrompt");
            String systemPrompt = loaderUtils.load(CHECKPOINT_DAILY_SUMMARY.getPath());
            log.info("SystemPrompt cargado correctamente");

            String llmResponse = chatSpringAI.generate("",systemPrompt, prompt);

            ResumenDiarioResponse response = parseDailySummaryResponse(llmResponse, request.userId());
            log.info("Resumen diario generado exitosamente | userId='{}' sugerencias={}",
                    request.userId(), response.sugerencias().size());
            return response;
        } catch (Exception e) {
            log.error("Error generando resumen diario para userId='{}': {}", request.userId(), e.getMessage(), e);
            return new ResumenDiarioResponse(
                    "No fue posible generar el resumen ejecutivo automáticamente. Revisa el log para más detalles.",
                    fallbackSugerencias()
            );
        }
    }

    // ─── Helpers ────────────────────────────────────────────────────────────────

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

    private String extractJson(String llmResponse) {
        int start = llmResponse.indexOf('{');
        int end = llmResponse.lastIndexOf('}');
        if (start == -1 || end == -1 || start > end) {
            throw new IllegalArgumentException("La respuesta del LLM no contiene JSON válido");
        }
        return llmResponse.substring(start, end + 1);
    }

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
