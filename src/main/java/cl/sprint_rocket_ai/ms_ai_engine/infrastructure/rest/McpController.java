package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.MCPQueryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "MCP Engine",
        description = "Gateway de interacción con el motor de IA usando herramientas MCP"
)
public interface McpController {

    @Operation(
            summary = "Procesar consultas de Actividades",
            description = """
            Permite interactuar con el agente de IA para crear, buscar o listar actividades del desarrollador. 
            El modelo decidirá de forma autónoma si invoca las herramientas MCP correspondientes.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta procesada exitosamente por el agente de actividades",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Request inválido o faltan parámetros obligatorios", content = @Content)
    })
    @PostMapping("/actividades")
    ResponseEntity<String> actividadTool(@Valid @RequestBody MCPQueryRequest request);

    @Operation(
            summary = "Chat con agente Jira",
            description = """
            Permite interactuar con Jira mediante lenguaje natural. 
            El modelo puede buscar issues, crear tickets, añadir comentarios o cambiar estados usando herramientas MCP de Jira.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta procesada exitosamente por el agente de Jira",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Estructura del request incorrecta", content = @Content)
    })
    @PostMapping("/jira")
    ResponseEntity<String> jiraTool(@Valid @RequestBody MCPQueryRequest request);

    @Operation(
            summary = "Procesar consultas de Recordatorios",
            description = """
            Permite interactuar con el agente especialista en alertas y recordatorios basados en tiempo. 
            Transforma el lenguaje natural en programaciones horarias precisas mediante MCP.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recordatorio procesado o listado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Error en el formato de fecha u otros campos obligatorios", content = @Content)
    })
    @PostMapping("/recordatorios")
    ResponseEntity<String> recordatorioTool(@Valid @RequestBody MCPQueryRequest request);
}