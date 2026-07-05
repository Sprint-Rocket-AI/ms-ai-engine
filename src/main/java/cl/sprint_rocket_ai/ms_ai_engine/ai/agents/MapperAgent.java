package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.MapperPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.PromptMapperRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class MapperAgent {
    private static final Logger log = LoggerFactory.getLogger(MapperAgent.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final MapperPromptBuilder promptBuilder;
    private final ChatSpringAI chatSpringAI;
    private final ObjectMapper mapper;

    public MapperAgent(SystemPromptLoaderUtils loaderUtils,
                       MapperPromptBuilder promptBuilder,
                       ChatSpringAI chatSpringAI,
                       ObjectMapper objectMapper
                       ) {
        this.loaderUtils = loaderUtils;
        this.promptBuilder = promptBuilder;
        this.chatSpringAI = chatSpringAI;
        this.mapper = objectMapper;
    }

    public Map<String, Object> map (PromptMapperRequest request){
        log.info("Iniciando Prompt Mapper");
        String userPrompt = request.content();
        log.info("Iniciando prompt: {}",userPrompt);
        String prompt = promptBuilder.build(userPrompt,request.template());
        log.info("Prompt creado, cargando systemPrompt");
        String systemPrompt = loaderUtils.load(promptBuilder.getType().getPathSystemPrompt());
        log.info("SystemPrompt cargado correctamente");
        String jsonString = chatSpringAI.generate(systemPrompt,prompt);
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
