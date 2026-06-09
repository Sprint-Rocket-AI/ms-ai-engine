package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.MapperPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.PromptMapperRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Map;

import static cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.SystemPromptTypeEnum.MAPPER;

@Service
public class MapperAgent {
    private static final Logger log = LoggerFactory.getLogger(MapperAgent.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final MapperPromptBuilder promptBuilder;
    private final ChatSpringAI chatSpringAI;
    private final ObjectMapper mapper;

    public MapperAgent(SystemPromptLoaderUtils loaderUtils, MapperPromptBuilder promptBuilder, ChatSpringAI chatSpringAI) {
        this.loaderUtils = loaderUtils;
        this.promptBuilder = promptBuilder;
        this.chatSpringAI = chatSpringAI;
        this.mapper = new ObjectMapper();
    }

    public Map<String, Object> map (PromptMapperRequest request){
        String sessionId = request.sessionId();
        log.info("Iniciando Prompt Mapper sessionId: {}",sessionId);
        String userPrompt = request.content();
        log.info("Iniciando prompt: {}",userPrompt);
        String prompt = promptBuilder.build(userPrompt,request.template());
        log.info("Prompt creado, cargando systemPrompt");
        String systemPrompt = loaderUtils.load(MAPPER.getPath());
        log.info("SystemPrompt cargado correctamente");
        String jsonString = chatSpringAI.generate(sessionId,systemPrompt,prompt);
        log.info("Fin de Prompt Mapper");
        return toMapClass(jsonString);
    }

    private Map<String, Object> toMapClass(String jsonString){
        try {
            return mapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }




}
