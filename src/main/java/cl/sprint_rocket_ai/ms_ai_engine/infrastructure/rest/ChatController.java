package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.ChatMessageResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.ChatResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.CreateChatRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.chat.CreateChatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Chat", description = "Endpoints de gestion de chats y mensajes")
public interface ChatController {

    @Operation(summary = "Crear chat", description = "Crea una nueva sesion de chat para un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chat creado",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Request invalida", content = @Content)
    })
    ResponseEntity<CreateChatResponse> create(@Valid @RequestBody CreateChatRequest request);

    @Operation(summary = "Listar chats por usuario", description = "Obtiene la lista de chats asociados a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de chats",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Request invalida", content = @Content)
    })
    ResponseEntity<List<ChatResponse>> getChatsByUserId(@Valid @RequestBody String userId);

    @Operation(summary = "Obtener mensajes de un chat", description = "Obtiene el historial completo de mensajes de una sesion de chat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historial de mensajes",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatMessageResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Sesion no encontrada", content = @Content)
    })
    ResponseEntity<List<ChatMessageResponse>> getMessagesChatBySessionId(
            @Parameter(description = "Id de la sesion de chat", required = true) @PathVariable String sessionId);

    @Operation(summary = "Eliminar chat por sessionId", description = "Elimina la sesion de chat y todos sus mensajes asociados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operacion completada", content = @Content)
    })
    ResponseEntity<Void> deleteChatBySessionId(
            @Parameter(description = "Id de la sesion de chat", required = true) @PathVariable String sessionId);
}

