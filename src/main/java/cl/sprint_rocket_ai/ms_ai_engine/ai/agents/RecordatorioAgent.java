package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.RecordatorioPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.MCPQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RecordatorioAgent {
    private static final Logger log = LoggerFactory.getLogger(RecordatorioAgent.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final RecordatorioPromptBuilder promptBuilder;
    private final ChatSpringAI chatSpringAI;

    public RecordatorioAgent(SystemPromptLoaderUtils loaderUtils,
                                  RecordatorioPromptBuilder promptBuilder,
                                  ChatSpringAI chatSpringAI) {
        this.loaderUtils = loaderUtils;
        this.promptBuilder = promptBuilder;
        this.chatSpringAI = chatSpringAI;
    }

    public String recordatorioChat(MCPQueryRequest request) {
        String sessionId = request.sessionId();

        log.info("Iniciando Suggest Activities Agent sessionId: {}", sessionId);
        LocalDateTime fechaActual = LocalDateTime.now();
        String prompt = promptBuilder.build(request.query(), request.userId(),fechaActual);
        log.info("Prompt creado, cargando systemPrompt");

        String systemPrompt = loaderUtils.load(promptBuilder.getType().getPathSystemPrompt());
        log.info("SystemPrompt cargado correctamente");

        String response = chatSpringAI.generate(sessionId, systemPrompt, prompt);
        log.info("Fin de Suggest Activities Agent");

        return response;
    }

}
