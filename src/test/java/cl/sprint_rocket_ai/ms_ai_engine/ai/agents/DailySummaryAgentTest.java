package cl.sprint_rocket_ai.ms_ai_engine.ai.agents;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.DailySummaryPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.ResumenDiarioRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.ResumenDiarioResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.actividad.ActividadItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailySummaryAgentTest {

    private static final String USER_ID = "dev-matias-001";
    private static final LocalDate FECHA = LocalDate.of(2026, 6, 5);
    private static final String TITULO_ACTIVIDAD = "Implementar autenticación OAuth2";
    private static final String DESCRIPCION_ACTIVIDAD = "Configurar flujo de autorización con provider externo";
    private static final String ESTADO_ACTIVIDAD = "COMPLETADA";
    private static final String PRIORIDAD_ACTIVIDAD = "ALTA";
    private static final String PATH_SYSTEM_PROMPT = "system_prompts/CHECKPOINT_DAILY_SUMMARY.txt";
    private static final String PROMPT_GENERADO = "prompt-resumen";
    private static final String JSON_RESPUESTA = "{\"summary\":\"ok\"}";

    @Mock
    private SystemPromptLoaderUtils loaderUtils;

    @Mock
    private DailySummaryPromptBuilder promptBuilder;

    @Mock
    private ChatSpringAI chatSpringAI;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResumenDiarioResponse resumenDiarioResponse;

    @InjectMocks
    private DailySummaryAgent dailySummaryAgent;

    @Test
    @DisplayName("Debe construir el prompt del resumen diario y cargar el system prompt correcto")
    void shouldWhenConstruyeElPromptYCargaElSystemPromptCorrecto() throws Exception {
        // Given
        ActividadItem actividadItem = new ActividadItem(
                TITULO_ACTIVIDAD,
                DESCRIPCION_ACTIVIDAD,
                ESTADO_ACTIVIDAD,
                PRIORIDAD_ACTIVIDAD
        );
        ResumenDiarioRequest request = new ResumenDiarioRequest(USER_ID, FECHA, List.of(actividadItem));

        when(promptBuilder.buildSummaryPrompt(USER_ID, FECHA, request.actividades())).thenReturn(PROMPT_GENERADO);
        when(promptBuilder.getType()).thenReturn(PromptTypeEnum.CHECKPOINT_DAILY_SUMMARY);
        when(loaderUtils.load(PATH_SYSTEM_PROMPT)).thenReturn("system-prompt");
        when(chatSpringAI.generate("system-prompt", PROMPT_GENERADO)).thenReturn(JSON_RESPUESTA);
        when(objectMapper.readValue(JSON_RESPUESTA, ResumenDiarioResponse.class)).thenReturn(resumenDiarioResponse);

        // When
        dailySummaryAgent.dailySummary(request);

        // Then
        verify(promptBuilder).buildSummaryPrompt(USER_ID, FECHA, request.actividades());
        verify(promptBuilder).getType();
        verify(loaderUtils).load(PATH_SYSTEM_PROMPT);
        verify(chatSpringAI).generate("system-prompt", PROMPT_GENERADO);
        verifyNoMoreInteractions(promptBuilder, loaderUtils, chatSpringAI);
    }
}
