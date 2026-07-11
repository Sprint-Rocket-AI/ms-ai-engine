package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.ChatController;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.ChatMessageResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.ChatResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.CreateChatRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.CreateChatResponse;
import cl.sprint_rocket_ai.ms_ai_engine.service.ChatMessageService;
import cl.sprint_rocket_ai.ms_ai_engine.service.ChatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/chat")
public class ChatRestController implements ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatRestController.class);

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    public ChatRestController(ChatService chatService, ChatMessageService chatMessageService) {
        this.chatService = chatService;
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ResponseEntity<CreateChatResponse> create(@Valid @RequestBody CreateChatRequest request) {
        return ResponseEntity.ok(chatService.createChat(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatResponse>> getChatsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(chatService.getChatsByUserId(userId));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<List<ChatMessageResponse>> getMessagesChatBySessionId(@PathVariable String sessionId){
        return ResponseEntity.ok(chatMessageService.getFullHistory(sessionId));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteChatBySessionId(@PathVariable String sessionId) {
        long deletedMessages = chatMessageService.deleteMessagesBySessionId(sessionId);
        long deletedChats = chatService.deleteChatBySessionId(sessionId);
        log.info("Eliminacion completada para sessionId: {}. chats={}, mensajes={}", sessionId, deletedChats, deletedMessages);
        return ResponseEntity.noContent().build();
    }
}

