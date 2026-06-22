package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.MCPQueryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "MCP Engine",
        description = "Gateway de interacción con el motor de IA usando herramientas MCP"
)
public interface McpApi {

    @Operation(
            summary = "Test general del motor MCP",
            description = """
            Permite interactuar con el motor de IA usando herramientas MCP relacionadas a actividades.
            El modelo puede decidir si usar herramientas o responder directamente.
            """
    )
    @PostMapping("/test")
    String test(
            @Valid @RequestBody MCPQueryRequest request
    );

    @Operation(
            summary = "Chat con agente Jira (MCP + Tools)",
            description = """
            Permite interactuar con Jira mediante lenguaje natural.
            El modelo puede buscar issues, crear tickets, comentar y cambiar estados
            usando herramientas MCP de Jira.
            """
    )
    @PostMapping("/jira")
    String chatWithJira(
            @Valid @RequestBody MCPQueryRequest request
    );
}