package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.MapperPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.PromptMapperRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapperAgentTest {

    private static final String CONTENT = "Mapea esta estructura";
    private static final String PROMPT_GENERADO = "prompt-mapper";
    private static final String SYSTEM_PROMPT = "system-prompt-mapper";
    private static final String PATH_SYSTEM_PROMPT = "system_prompts/MAPPER.txt";

    @Mock
    private SystemPromptLoaderUtils loaderUtils;

    @Mock
    private MapperPromptBuilder promptBuilder;

    @Mock
    private ChatSpringAI chatSpringAI;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MapperAgent mapperAgent;

    @Test
    @DisplayName("Debe construir el prompt del mapper y cargar el system prompt correcto")
    void shouldWhenConstruyePromptYCargaSystemPrompt() throws Exception {
        // Given
        Map<String, Object> template = new LinkedHashMap<>();
        template.put("campo1", "valor1");
        PromptMapperRequest request = new PromptMapperRequest(CONTENT, template);

        when(promptBuilder.build(CONTENT, template)).thenReturn(PROMPT_GENERADO);
        when(promptBuilder.getType()).thenReturn(PromptTypeEnum.MAPPER);
        when(loaderUtils.load(PATH_SYSTEM_PROMPT)).thenReturn(SYSTEM_PROMPT);
        when(chatSpringAI.generate(SYSTEM_PROMPT, PROMPT_GENERADO)).thenReturn("{\"ok\":true}");

        // When
        mapperAgent.map(request);

        // Then
        verify(promptBuilder).build(CONTENT, template);
        verify(promptBuilder).getType();
        verify(loaderUtils).load(PATH_SYSTEM_PROMPT);
        verify(chatSpringAI).generate(SYSTEM_PROMPT, PROMPT_GENERADO);
        verifyNoMoreInteractions(promptBuilder, loaderUtils, chatSpringAI);
    }
}
