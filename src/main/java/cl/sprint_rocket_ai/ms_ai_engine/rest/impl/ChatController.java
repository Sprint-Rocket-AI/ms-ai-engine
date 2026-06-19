package cl.sprint_rocket_ai.ms_ai_engine.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.chat.ChatByUserIdRequest;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.chat.ChatMessageResponse;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.chat.ChatResponse;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.chat.CreateChatRequest;
import cl.sprint_rocket_ai.ms_ai_engine.service.ChatMessageService;
import cl.sprint_rocket_ai.ms_ai_engine.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    public ChatController(ChatService chatService,ChatMessageService chatMessageService) {
        this.chatService = chatService;
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateChatRequest request) {
        return ResponseEntity.ok(chatService.createChat(request));
    }

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChatsByUserId(@Valid @RequestBody ChatByUserIdRequest request) {
        return ResponseEntity.ok(chatService.getChatsByUserId(request.userId()));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<List<ChatMessageResponse>> getMessagesChatBySessionId(@PathVariable String sessionId){
        return ResponseEntity.ok(chatMessageService.getFullHistory(sessionId));
    }
}

