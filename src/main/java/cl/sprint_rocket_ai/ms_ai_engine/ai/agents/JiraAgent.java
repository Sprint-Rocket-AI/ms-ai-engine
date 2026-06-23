package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.JiraPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.MCPQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.SystemPromptTypeEnum.JIRA_TOOL;

@Service
public class JiraAgent {

    private static final Logger log = LoggerFactory.getLogger(JiraAgent.class);

    private final SystemPromptLoaderUtils loaderUtils;
    private final JiraPromptBuilder promptBuilder;
    private final ChatSpringAI chatSpringAI;

    public JiraAgent(
            SystemPromptLoaderUtils loaderUtils,
            ChatSpringAI chatSpringAI,
            JiraPromptBuilder promptBuilder) {

        this.loaderUtils = loaderUtils;
        this.chatSpringAI = chatSpringAI;
        this.promptBuilder = promptBuilder;
    }

    public String jiraChat(MCPQueryRequest request) {

        String sessionId = request.sessionId();
        log.info("Iniciando JiraAgent sessionId: {}", sessionId);
        String prompt = promptBuilder.build(request.query(), request.userId());
        log.info("Prompt creado, cargando systemPrompt");

        String systemPrompt = loaderUtils.load(JIRA_TOOL.getPath());
        log.info("SystemPrompt cargado correctamente");

        String response = chatSpringAI.generate(sessionId, systemPrompt, prompt);
        log.info("Fin de JiraAgent");

        return response;
    }
}
