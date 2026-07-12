package cl.sprint_rocket_ai.ms_ai_engine.service;

import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.ChatMessage;
import cl.sprint_rocket_ai.ms_ai_engine.domain.documents.Role;
import cl.sprint_rocket_ai.ms_ai_engine.domain.repositories.ChatMessageMongoRepository;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.ChatMessageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    private static final String SESSION_ID = "session-01";
    private static final String MESSAGE_1 = "hola";
    private static final String MESSAGE_2 = "respuesta";
    private static final Instant TIME_1 = Instant.parse("2026-07-12T10:00:00Z");
    private static final Instant TIME_2 = Instant.parse("2026-07-12T10:01:00Z");

    @Mock
    private ChatMessageMongoRepository messageRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Test
    @DisplayName("Debe mapear el historial completo del chat")
    void shouldWhenObtieneHistorialMapeaLosMensajes() {
        // Given
        ChatMessage message1 = new ChatMessage();
        message1.setRole(Role.USER);
        message1.setContent(MESSAGE_1);
        message1.setTimestamp(TIME_1);

        ChatMessage message2 = new ChatMessage();
        message2.setRole(Role.ASSISTANT);
        message2.setContent(MESSAGE_2);
        message2.setTimestamp(TIME_2);

        when(messageRepository.findBySessionIdOrderByTimestampAsc(SESSION_ID)).thenReturn(List.of(message1, message2));

        // When
        List<ChatMessageResponse> response = chatMessageService.getFullHistory(SESSION_ID);

        // Then
        assertEquals(2, response.size());
        assertEquals(Role.USER, response.get(0).role());
        assertEquals(MESSAGE_1, response.get(0).content());
        assertEquals(TIME_1, response.get(0).timestamp());
        assertEquals(Role.ASSISTANT, response.get(1).role());
        assertEquals(MESSAGE_2, response.get(1).content());
        assertEquals(TIME_2, response.get(1).timestamp());
        verify(messageRepository).findBySessionIdOrderByTimestampAsc(SESSION_ID);
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    @DisplayName("Debe eliminar los mensajes por sessionId")
    void shouldWhenEliminaMensajesPorSessionIdRetornaCantidad() {
        // Given
        when(messageRepository.deleteBySessionId(SESSION_ID)).thenReturn(2L);

        // When
        long deletedCount = chatMessageService.deleteMessagesBySessionId(SESSION_ID);

        // Then
        assertEquals(2L, deletedCount);
        verify(messageRepository).deleteBySessionId(SESSION_ID);
        verifyNoMoreInteractions(messageRepository);
    }
}
