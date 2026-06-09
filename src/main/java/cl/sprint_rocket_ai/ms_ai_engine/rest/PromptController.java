package cl.sprint_rocket_ai.ms_ai_engine.rest;

import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.AIResponse;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.PromptMapperRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Tag(name = "Prompt", description = "Endpoints de Prompts customizados")
public interface PromptController {
    @Operation(summary = "Mapper", description = "Desde el contenido envíado formatea a un Json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contenido formateado a json",
                    content = @Content(schema = @Schema(implementation = AIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Request invalida", content = @Content)
    })
    ResponseEntity<Map<String,Object>> mapper(@Valid @RequestBody PromptMapperRequest request);
}
