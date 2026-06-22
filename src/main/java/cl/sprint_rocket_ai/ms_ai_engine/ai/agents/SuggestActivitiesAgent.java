package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.SuggestActivitiesPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.SugerirActividadesRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.SugerirActividadesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.SystemPromptTypeEnum.CHECKPOINT_SUGGEST;

@Service
public class SuggestActivitiesAgent {

    private static final Logger log = LoggerFactory.getLogger(SuggestActivitiesAgent.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final SuggestActivitiesPromptBuilder promptBuilder;
    private final ChatSpringAI chatSpringAI;
    private final ObjectMapper mapper;

    public SuggestActivitiesAgent(SystemPromptLoaderUtils loaderUtils,
                                  SuggestActivitiesPromptBuilder promptBuilder,
                                  ChatSpringAI chatSpringAI) {
        this.loaderUtils = loaderUtils;
        this.promptBuilder = promptBuilder;
        this.chatSpringAI = chatSpringAI;
        this.mapper = new ObjectMapper();
    }

    public SugerirActividadesResponse suggest(SugerirActividadesRequest request) {
        String sessionId = request.userId();
        log.info("Iniciando Suggest Activities Agent sessionId: {}", sessionId);
        log.info("Parámetros - fecha: {}, actividades: {}", request.fecha(), request.actividades());

        String prompt = promptBuilder.buildSuggestionPrompt(request.userId(), request.fecha(), request.actividades());
        log.info("Prompt creado, cargando systemPrompt");

        String systemPrompt = loaderUtils.load(CHECKPOINT_SUGGEST.getPath());
        log.info("SystemPrompt cargado correctamente");

        String jsonString = chatSpringAI.generate(sessionId, systemPrompt, prompt);
        log.info("Fin de Suggest Activities Agent");

        return toResponseClass(jsonString);
    }

    private SugerirActividadesResponse toResponseClass(String jsonString) {
        try {
            return mapper.readValue(jsonString, SugerirActividadesResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar respuesta de sugerencias", e);
        }
    }
}
