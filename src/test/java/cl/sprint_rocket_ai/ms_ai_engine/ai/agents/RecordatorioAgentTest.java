package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.RecordatorioPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.MCPQueryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordatorioAgentTest {

    private static final String QUERY = "Recuérdame revisar el ticket mañana";
    private static final String USER_ID = "dev-02";
    private static final String SESSION_ID = "session-002";
    private static final String PROMPT_GENERADO = "prompt-recordatorio";
    private static final String SYSTEM_PROMPT = "system-prompt-recordatorio";
    private static final String PATH_SYSTEM_PROMPT = "system_prompts/RECORDATORIO_TOOL.txt";

    @Mock
    private SystemPromptLoaderUtils loaderUtils;

    @Mock
    private RecordatorioPromptBuilder promptBuilder;

    @Mock
    private ChatSpringAI chatSpringAI;

    @InjectMocks
    private RecordatorioAgent recordatorioAgent;

    @Test
    @DisplayName("Debe construir el prompt de recordatorio y cargar el system prompt correcto")
    void shouldWhenConstruyePromptYCargaSystemPrompt() {
        // Given
        MCPQueryRequest request = new MCPQueryRequest(QUERY, USER_ID, SESSION_ID);

        when(promptBuilder.build(eq(QUERY), eq(USER_ID), any(LocalDateTime.class))).thenReturn(PROMPT_GENERADO);
        when(promptBuilder.getType()).thenReturn(PromptTypeEnum.RECORDATORIO_TOOL);
        when(loaderUtils.load(PATH_SYSTEM_PROMPT)).thenReturn(SYSTEM_PROMPT);
        when(chatSpringAI.generate(SESSION_ID, SYSTEM_PROMPT, PROMPT_GENERADO)).thenReturn("response");

        // When
        recordatorioAgent.recordatorioChat(request);

        // Then
        verify(promptBuilder).build(eq(QUERY), eq(USER_ID), any(LocalDateTime.class));
        verify(promptBuilder).getType();
        verify(loaderUtils).load(PATH_SYSTEM_PROMPT);
        verify(chatSpringAI).generate(SESSION_ID, SYSTEM_PROMPT, PROMPT_GENERADO);
        verifyNoMoreInteractions(promptBuilder, loaderUtils, chatSpringAI);
    }
}
