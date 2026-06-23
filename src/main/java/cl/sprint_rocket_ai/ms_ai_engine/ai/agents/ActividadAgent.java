package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.ActividadPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.MCPQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.SystemPromptTypeEnum.ACTIVIDAD_TOOL;

@Service
public class ActividadAgent {
    private static final Logger log = LoggerFactory.getLogger(ActividadAgent.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final ActividadPromptBuilder promptBuilder;
    private final ChatSpringAI chatSpringAI;

    public ActividadAgent(SystemPromptLoaderUtils loaderUtils,
                          ActividadPromptBuilder promptBuilder,
                             ChatSpringAI chatSpringAI) {
        this.loaderUtils = loaderUtils;
        this.promptBuilder = promptBuilder;
        this.chatSpringAI = chatSpringAI;
    }

    public String actividadChat(MCPQueryRequest request) {
        String sessionId = request.sessionId();

        log.info("Iniciando  ActividadAgent sessionId: {}", sessionId);
        String prompt = promptBuilder.build(request.query(), request.userId());
        log.info("Prompt creado, cargando systemPrompt");
        String systemPrompt = loaderUtils.load(ACTIVIDAD_TOOL.getPath());
        log.info("SystemPrompt cargado correctamente");

        String response = chatSpringAI.generate(sessionId, systemPrompt, prompt);
        log.info("Fin de Suggest Activities Agent");

        return response;
    }

}
