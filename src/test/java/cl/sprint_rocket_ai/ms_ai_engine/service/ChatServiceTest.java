package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.Chat;
import cl.sprint_rocket_ai.ms_ai_engine.domain.repositories.ChatMongoRepository;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.AIRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.ChatResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.CreateChatRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.CreateChatResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    private static final String USER_ID = "dev-01";
    private static final String CONTENT = "Crear un ticket para corregir el login bug";
    private static final String CHAT_TITLE = "Crear un ticket para";
    private static final String SESSION_ID = "session-123";
    private static final Instant CREATED_AT = Instant.parse("2026-07-12T16:00:00Z");
    private static final String ANSWER = "respuesta-mock";
    private static final String SESSION_ID_2 = "session-456";
    private static final String TITLE_2 = "Segundo chat";
    private static final Instant CREATED_AT_2 = Instant.parse("2026-07-11T16:00:00Z");
    private static final String SESSION_ID_DELETE = "session-delete";

    @Mock
    private ChatMongoRepository chatMongoRepository;

    @Mock
    private RAGService ragService;

    @InjectMocks
    private ChatService chatService;

    @Test
    @DisplayName("Debe crear el chat con sessionId, título y fecha, y llamar al ask con la request correcta")
    void shouldWhenCreaChatSeteaDatosYConsultaAsk() {
        // Given
        CreateChatRequest request = new CreateChatRequest(USER_ID, CONTENT);

        when(chatMongoRepository.save(org.mockito.ArgumentMatchers.any(Chat.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(ragService.ask(org.mockito.ArgumentMatchers.any(AIRequest.class))).thenReturn(ANSWER);

        // When
        CreateChatResponse response = chatService.createChat(request);

        // Then
        ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);
        ArgumentCaptor<AIRequest> aiRequestCaptor = ArgumentCaptor.forClass(AIRequest.class);

        verify(chatMongoRepository).save(chatCaptor.capture());
        verify(ragService).ask(aiRequestCaptor.capture());
        verifyNoMoreInteractions(chatMongoRepository, ragService);

        Chat chat = chatCaptor.getValue();
        AIRequest aiRequest = aiRequestCaptor.getValue();

        assertNotNull(chat.getSessionId());
        assertEquals(USER_ID, chat.getUserId());
        assertEquals(CHAT_TITLE, chat.getTitle());
        assertNotNull(chat.getCreatedAt());

        assertEquals(chat.getSessionId(), aiRequest.sessionId());
        assertEquals(CONTENT, aiRequest.userPrompt());

        assertEquals(chat.getSessionId(), response.sessionId());
        assertEquals(CHAT_TITLE, response.title());
        assertEquals(CONTENT, response.query());
        assertEquals(ANSWER, response.answer());
        assertEquals(chat.getCreatedAt(), response.createdAt());
        assertNotNull(response.createdAt());
    }

    @Test
    @DisplayName("Debe devolver los chats del usuario mapeados desde el repositorio")
    void shouldWhenObtieneChatsPorUsuarioMapeaLaRespuesta() {
        // Given
        Chat chat1 = new Chat();
        chat1.setSessionId(SESSION_ID);
        chat1.setTitle(CHAT_TITLE);
        chat1.setCreatedAt(CREATED_AT);

        Chat chat2 = new Chat();
        chat2.setSessionId(SESSION_ID_2);
        chat2.setTitle(TITLE_2);
        chat2.setCreatedAt(CREATED_AT_2);

        List<Chat> chats = List.of(chat1, chat2);
        when(chatMongoRepository.findByUserIdOrderByCreatedAtDesc(USER_ID)).thenReturn(chats);

        // When
        List<ChatResponse> response = chatService.getChatsByUserId(USER_ID);

        // Then
        verify(chatMongoRepository).findByUserIdOrderByCreatedAtDesc(USER_ID);
        verifyNoMoreInteractions(chatMongoRepository, ragService);

        assertEquals(2, response.size());
        assertEquals(CHAT_TITLE, response.get(0).title());
        assertEquals(SESSION_ID, response.get(0).sessionId());
        assertEquals(CREATED_AT, response.get(0).createdAt());
        assertEquals(TITLE_2, response.get(1).title());
        assertEquals(SESSION_ID_2, response.get(1).sessionId());
        assertEquals(CREATED_AT_2, response.get(1).createdAt());
    }

    @Test
    @DisplayName("Debe eliminar el chat por sessionId y devolver la cantidad eliminada")
    void shouldWhenEliminaChatPorSessionIdRetornarCantidad() {
        // Given
        when(chatMongoRepository.deleteBySessionId(SESSION_ID_DELETE)).thenReturn(1L);

        // When
        long deletedCount = chatService.deleteChatBySessionId(SESSION_ID_DELETE);

        // Then
        verify(chatMongoRepository).deleteBySessionId(SESSION_ID_DELETE);
        verifyNoMoreInteractions(chatMongoRepository, ragService);
        assertEquals(1L, deletedCount);
    }
}
