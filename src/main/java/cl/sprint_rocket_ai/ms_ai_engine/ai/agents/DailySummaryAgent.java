package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.DailySummaryPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.ResumenDiarioRequest;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.ResumenDiarioResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.SystemPromptTypeEnum.CHECKPOINT_DAILY_SUMMARY;

@Service
public class DailySummaryAgent {

    private static final Logger log = LoggerFactory.getLogger(DailySummaryAgent.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final DailySummaryPromptBuilder promptBuilder;
    private final ChatSpringAI chatSpringAI;
    private final ObjectMapper mapper;

    public DailySummaryAgent(SystemPromptLoaderUtils loaderUtils,
                             DailySummaryPromptBuilder promptBuilder,
                             ChatSpringAI chatSpringAI) {
        this.loaderUtils = loaderUtils;
        this.promptBuilder = promptBuilder;
        this.chatSpringAI = chatSpringAI;
        this.mapper = new ObjectMapper();
    }

    public ResumenDiarioResponse dailySummary(ResumenDiarioRequest request) {
        String sessionId = request.userId();
        log.info("Iniciando Daily Summary Agent sessionId: {}", sessionId);
        log.info("Parámetros - fecha: {}, actividades: {}", request.fecha(), request.actividades().size());

        String prompt = promptBuilder.buildSummaryPrompt(request.userId(), request.fecha(), request.actividades());
        log.info("Prompt creado, cargando systemPrompt");

        String systemPrompt = loaderUtils.load(CHECKPOINT_DAILY_SUMMARY.getPath());
        log.info("SystemPrompt cargado correctamente");

        String jsonString = chatSpringAI.generate(sessionId, systemPrompt, prompt);
        log.info("Fin de Daily Summary Agent");

        return toResponseClass(jsonString);
    }

    private ResumenDiarioResponse toResponseClass(String jsonString) {
        try {
            return mapper.readValue(jsonString, ResumenDiarioResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar respuesta de resumen diario", e);
        }
    }
}
