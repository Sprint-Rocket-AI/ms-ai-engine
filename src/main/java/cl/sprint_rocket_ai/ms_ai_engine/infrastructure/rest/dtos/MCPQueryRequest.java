package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Estructura de la petición para interactuar con el motor AI y MCP")
public record MCPQueryRequest(

        @Schema(
                description = "Consulta o instrucción en lenguaje natural para el asistente AI",
                example = "Crea un ticket en Jira para corregir el login bug y muévelo a In Progress",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La consulta no puede estar vacía")
        String query,

        @Schema(
                description = "Identificador del usuario que ejecuta la consulta",
                example = "dev_01",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String userId,

        @Schema(
                description = "Identificador de sesión para mantener contexto conversacional",
                example = "session-123",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        String sessionId
) {}