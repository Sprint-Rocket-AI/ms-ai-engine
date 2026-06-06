package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.builders.MapperPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.PromptMapperRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import static cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.SystemPromptTypeEnum.MAPPER;

@Service
public class PromptService {
    private static final Logger log = LoggerFactory.getLogger(PromptService.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final MapperPromptBuilder promptBuilder;
    private final LLMPortOut llmPortOut;

    public PromptService(SystemPromptLoaderUtils loaderUtils, MapperPromptBuilder promptBuilder, LLMPortOut llmPortOut) {
        this.loaderUtils = loaderUtils;
        this.promptBuilder = promptBuilder;
        this.llmPortOut = llmPortOut;
    }

    public String mapper (PromptMapperRequest request){
        log.info("Iniciando Prompt Mapper");
        String userPrompt = request.content();
        log.info("Iniciando prompt: {}",userPrompt);
        String prompt = promptBuilder.build(userPrompt,request.template());
        log.info("Prompt creado, cargando systemPrompt");
        String systemPrompt = loaderUtils.load(MAPPER.getPath());
        String answer = llmPortOut.generate(systemPrompt,prompt);
        log.info("Fin de Prompt Mapper");
        return answer;
    }




}
