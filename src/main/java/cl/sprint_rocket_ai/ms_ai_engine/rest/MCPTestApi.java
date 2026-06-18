package cl.sprint_rocket_ai.ms_ai_engine.rest;

import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.MCPQueryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "MCP Engine Test", description = "Endpoints de prueba para la integración con Model Context Protocol (MCP)")
public interface MCPTestApi {

    @Operation(
            summary = "Evaluar un prompt mediante una petición POST utilizando el motor AI y herramientas MCP",
            description = "Envía una consulta estructurada en el cuerpo de la petición hacia el modelo de lenguaje (Qwen). " +
                    "El modelo decidirá si requiere invocar herramientas del servidor MCP basándose en el texto provisto."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Procesamiento exitoso. Devuelve la respuesta final del asistente tras procesar o no las herramientas."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. El cuerpo de la solicitud no cumple con las validaciones del esquema."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno en el motor de IA o fallas de comunicación con el ecosistema MCP."
            )
    })
    @PostMapping
    String test(
            @RequestBody(description = "Cuerpo de la petición que contiene el prompt del usuario", required = true)
            @Valid MCPQueryRequest request
    );
}
