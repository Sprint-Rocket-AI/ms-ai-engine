package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.MapperPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.PromptMapperRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Map;

import static cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.SystemPromptTypeEnum.MAPPER;

@Service
public class PromptService {
    private static final Logger log = LoggerFactory.getLogger(PromptService.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final MapperPromptBuilder promptBuilder;
    private final LLMPortOut llmPortOut;
    private final ObjectMapper mapper;

    public PromptService(SystemPromptLoaderUtils loaderUtils, MapperPromptBuilder promptBuilder, LLMPortOut llmPortOut) {
        this.loaderUtils = loaderUtils;
        this.promptBuilder = promptBuilder;
        this.llmPortOut = llmPortOut;
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
        String jsonString = llmPortOut.generate(sessionId,systemPrompt,prompt);
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
