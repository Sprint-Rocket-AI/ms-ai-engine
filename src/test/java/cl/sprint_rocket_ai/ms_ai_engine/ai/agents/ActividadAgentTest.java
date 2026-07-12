package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.ActividadPromptBuilder;
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
class ActividadAgentTest {

    @Mock
    private SystemPromptLoaderUtils loaderUtils;

    @Mock
    private ActividadPromptBuilder promptBuilder;

    @Mock
    private ChatSpringAI chatSpringAI;

    @InjectMocks
    private ActividadAgent actividadAgent;

    @Test
    @DisplayName("Debe construir el prompt y cargar el system prompt usando la ruta del builder")
    void shouldWhenConstruyePromptYCargaSystemPrompt() {
        // Given
        MCPQueryRequest request = new MCPQueryRequest("Crear una actividad", "user-123", "session-456");
        String promptConstruido = "prompt-construido";
        String systemPrompt = "system-prompt";

        when(promptBuilder.build("Crear una actividad", "user-123")).thenReturn(promptConstruido);
        when(promptBuilder.getType()).thenReturn(PromptTypeEnum.ACTIVIDAD_TOOL);
        when(loaderUtils.load("system_prompts/ACTIVIDAD_TOOL.txt")).thenReturn(systemPrompt);
        when(chatSpringAI.generate("session-456", systemPrompt, promptConstruido)).thenReturn("response-mock");

        // When
        actividadAgent.actividadChat(request);

        // Then
        verify(promptBuilder).build("Crear una actividad", "user-123");
        verify(promptBuilder).getType();
        verify(loaderUtils).load("system_prompts/ACTIVIDAD_TOOL.txt");
        verify(chatSpringAI).generate("session-456", systemPrompt, promptConstruido);
        verifyNoMoreInteractions(promptBuilder, loaderUtils, chatSpringAI);
    }
}
