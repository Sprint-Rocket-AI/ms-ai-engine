package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.JiraPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.MCPQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JiraAgentTest {

    private static final String QUERY = "Crear un ticket para corregir el login";
    private static final String USER_ID = "dev-01";
    private static final String SESSION_ID = "session-001";
    private static final String PROMPT_GENERADO = "prompt-jira";
    private static final String SYSTEM_PROMPT = "system-prompt-jira";
    private static final String PATH_SYSTEM_PROMPT = "system_prompts/JIRA_TOOL.txt";

    @Mock
    private SystemPromptLoaderUtils loaderUtils;

    @Mock
    private ChatSpringAI chatSpringAI;

    @Mock
    private JiraPromptBuilder promptBuilder;

    @InjectMocks
    private JiraAgent jiraAgent;

    @Test
    @DisplayName("Debe construir el prompt de Jira y cargar el system prompt correcto")
    void shouldWhenConstruyePromptYCargaSystemPrompt() {
        // Given
        MCPQueryRequest request = new MCPQueryRequest(QUERY, USER_ID, SESSION_ID);

        when(promptBuilder.build(QUERY, USER_ID)).thenReturn(PROMPT_GENERADO);
        when(promptBuilder.getType()).thenReturn(PromptTypeEnum.JIRA_TOOL);
        when(loaderUtils.load(PATH_SYSTEM_PROMPT)).thenReturn(SYSTEM_PROMPT);
        when(chatSpringAI.generate(SESSION_ID, SYSTEM_PROMPT, PROMPT_GENERADO)).thenReturn("response");

        // When
        jiraAgent.jiraChat(request);

        // Then
        verify(promptBuilder).build(QUERY, USER_ID);
        verify(promptBuilder).getType();
        verify(loaderUtils).load(PATH_SYSTEM_PROMPT);
        verify(chatSpringAI).generate(SESSION_ID, SYSTEM_PROMPT, PROMPT_GENERADO);
        verifyNoMoreInteractions(promptBuilder, loaderUtils, chatSpringAI);
    }
}
