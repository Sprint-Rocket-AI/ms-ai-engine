package cl.sprint_rocket_ai.ms_ai_engine.rest.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Estructura de la petición para interactuar con el motor AI y MCP")
public record MCPQueryRequest(

        @Schema(
                description = "Consulta o instrucción en lenguaje natural para el asistente AI.",
                example = "Crea una actividad tipo BUG con prioridad ALTA para el desarrollador dev_01 titulada 'Error en login'",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La consulta no puede estar vacía")
        String query
) {}