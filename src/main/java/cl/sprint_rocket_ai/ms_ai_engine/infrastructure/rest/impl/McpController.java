package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.ChatSpringAI;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.McpApi;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.MCPQueryRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp-tools")
public class McpController implements McpApi {

    private final ChatSpringAI chatSpringAI;

    public McpController(ChatSpringAI chatSpringAI) {
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

    @Override
    public String chatWithJira(MCPQueryRequest request) {

        return chatSpringAI.generate(
                "jira-session",
                """
                        Eres un asistente experto en Jira.
                        
                        Puedes:
                        - buscar issues
                        - crear tickets
                        - comentar issues
                        - cambiar estados
                        
                        Usa las herramientas disponibles en JiraTools.
                        """,
                request.query()
        );
    }

}
