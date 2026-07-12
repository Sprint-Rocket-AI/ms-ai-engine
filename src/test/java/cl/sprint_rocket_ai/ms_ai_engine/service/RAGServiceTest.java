package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders.RAGPromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.utils.SystemPromptLoaderUtils;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.semantic_cache.SemanticCacheAdvisor;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.AIRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.document.Document;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RAGServiceTest {

    private static final String SESSION_ID = "session-01";
    private static final String QUESTION = "¿Cómo creo un ticket?";
    private static final String CACHED_ANSWER = "respuesta-cacheada";
    private static final String SYSTEM_PROMPT = "system-prompt";
    private static final String PATH_SYSTEM_PROMPT = "system_prompts/RAG.txt";
    private static final String PROMPT_WITH_CONTEXT = "prompt-context";
    private static final String PROMPT_WITHOUT_CONTEXT = "prompt-sin-contexto";
    private static final String CONTEXT = "contexto 1";
    private static final String ANSWER = "respuesta-generada";

    @Mock
    private ChatSpringAI chatSpringAI;

    @Mock
    private VectorStoreService vectorStoreService;

    @Mock
    private RAGPromptBuilder promptBuilder;

    @Mock
    private SystemPromptLoaderUtils loaderUtils;

    @Mock
    private SemanticCacheAdvisor semanticCache;

    @InjectMocks
    private RAGService ragService;

    @Test
    @DisplayName("Debe devolver la respuesta desde caché cuando existe una coincidencia")
    void shouldWhenEncuentraRespuestaEnCacheRetornaSinGenerar() {
        // Given
        AIRequest request = new AIRequest(SESSION_ID, QUESTION);
        when(semanticCache.findInCache(QUESTION)).thenReturn(Optional.of(CACHED_ANSWER));

        // When
        String response = ragService.ask(request);

        // Then
        assertEquals(CACHED_ANSWER, response);
        verify(semanticCache).findInCache(QUESTION);
        verifyNoMoreInteractions(semanticCache);
        verify(vectorStoreService, never()).search(any());
        verify(promptBuilder, never()).buildWithContext(any(), any());
        verify(promptBuilder, never()).build(any());
        verify(loaderUtils, never()).load(any());
        verify(chatSpringAI, never()).generate(any(), any(), any());
    }

    @Test
    @DisplayName("Debe construir el prompt con contexto y guardar la respuesta en caché")
    void shouldWhenNoEncuentraCacheConstruyePromptConContexto() {
        // Given
        AIRequest request = new AIRequest(SESSION_ID, QUESTION);
        List<Document> documents = List.of(Document.builder().text(CONTEXT).build());

        when(semanticCache.findInCache(QUESTION)).thenReturn(Optional.empty());
        when(vectorStoreService.search(QUESTION)).thenReturn(documents);
        when(promptBuilder.buildWithContext(QUESTION, CONTEXT)).thenReturn(PROMPT_WITH_CONTEXT);
        when(promptBuilder.getType()).thenReturn(PromptTypeEnum.RAG);
        when(loaderUtils.load(PATH_SYSTEM_PROMPT)).thenReturn(SYSTEM_PROMPT);
        when(chatSpringAI.generate(SESSION_ID, SYSTEM_PROMPT, PROMPT_WITH_CONTEXT)).thenReturn(ANSWER);

        // When
        String response = ragService.ask(request);

        // Then
        assertEquals(ANSWER, response);
        verify(semanticCache).findInCache(QUESTION);
        verify(vectorStoreService).search(QUESTION);
        verify(promptBuilder).buildWithContext(QUESTION, CONTEXT);
        verify(promptBuilder).getType();
        verify(loaderUtils).load(PATH_SYSTEM_PROMPT);
        verify(chatSpringAI).generate(SESSION_ID, SYSTEM_PROMPT, PROMPT_WITH_CONTEXT);
        verify(semanticCache).saveToCache(QUESTION, ANSWER);
        verifyNoMoreInteractions(vectorStoreService, promptBuilder, loaderUtils, chatSpringAI, semanticCache);
    }

    @Test
    @DisplayName("Debe construir el prompt base sin contexto y cargar el mismo system prompt")
    void shouldWhenNoEncuentraCacheConstruyePromptSinContexto() {
        // Given
        AIRequest request = new AIRequest(SESSION_ID, QUESTION);

        when(semanticCache.findInCache(QUESTION)).thenReturn(Optional.empty());
        when(vectorStoreService.search(QUESTION)).thenReturn(List.of());
        when(promptBuilder.build(QUESTION)).thenReturn(PROMPT_WITHOUT_CONTEXT);
        when(promptBuilder.getType()).thenReturn(PromptTypeEnum.RAG);
        when(loaderUtils.load(PATH_SYSTEM_PROMPT)).thenReturn(SYSTEM_PROMPT);
        when(chatSpringAI.generate(SESSION_ID, SYSTEM_PROMPT, PROMPT_WITHOUT_CONTEXT)).thenReturn(ANSWER);

        // When
        String response = ragService.ask(request);

        // Then
        assertEquals(ANSWER, response);
        verify(semanticCache).findInCache(QUESTION);
        verify(vectorStoreService).search(QUESTION);
        verify(promptBuilder).build(QUESTION);
        verify(promptBuilder).getType();
        verify(loaderUtils).load(PATH_SYSTEM_PROMPT);
        verify(chatSpringAI).generate(SESSION_ID, SYSTEM_PROMPT, PROMPT_WITHOUT_CONTEXT);
        verify(semanticCache).saveToCache(QUESTION, ANSWER);
        verifyNoMoreInteractions(vectorStoreService, promptBuilder, loaderUtils, chatSpringAI, semanticCache);
    }
}
