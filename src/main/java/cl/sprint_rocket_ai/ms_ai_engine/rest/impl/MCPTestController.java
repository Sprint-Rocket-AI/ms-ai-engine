package cl.sprint_rocket_ai.ms_ai_engine.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.rest.MCPTestApi;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.MCPQueryRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MCPTestController implements MCPTestApi {

    private final ChatSpringAI chatSpringAI;

    public MCPTestController(ChatSpringAI chatSpringAI) {
        this.chatSpringAI = chatSpringAI;
    }

    @Override
    public String test(@Valid @RequestBody MCPQueryRequest request) {
        return chatSpringAI.generate(
                "test-session",
                """
                Eres un asistente que puede usar herramientas MCP.

                Si el usuario solicita crear actividades,
                listar actividades o gestionar recordatorios,
                DEBES utilizar las herramientas disponibles.

                No inventes información.
                """,
                request.query() // Escuchamos el string encapsulado en el DTO
        );
    }
}
