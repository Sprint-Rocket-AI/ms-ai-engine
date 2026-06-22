package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.ResumenDiarioRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.ResumenDiarioResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.actividad.SugerirActividadesRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.actividad.SugerirActividadesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Checkpoint AI", description = "Endpoints de IA para el módulo Checkpoint: sugerencias y resumen diario")
public interface CheckpointRest {

    @Operation(
            summary = "Sugerir actividades de continuidad",
            description = """
                    Recibe la lista de actividades de un día específico de un desarrollador
                    y genera 5 sugerencias de actividades para dar continuidad al trabajo realizado.
                    Invocado por ms-checkpoint cuando el desarrollador solicita sugerencias para un día X.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "5 sugerencias de actividades generadas exitosamente",
                    content = @Content(schema = @Schema(implementation = SugerirActividadesResponse.class))),
            @ApiResponse(responseCode = "400", description = "Request inválida", content = @Content),
            @ApiResponse(responseCode = "503", description = "LLM no disponible", content = @Content)
    })
    ResponseEntity<SugerirActividadesResponse> sugerirActividades(
            @Valid @RequestBody SugerirActividadesRequest request);

    @Operation(
            summary = "Generar resumen diario ejecutivo",
            description = """
                    Recibe todas las actividades de un día (pendientes y completadas) de un desarrollador
                    y genera un resumen ejecutivo del trabajo realizado junto con sugerencias para el día siguiente.
                    Invocado por el scheduler de ms-checkpoint a las 8:30 a.m. de lunes a viernes.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resumen diario generado exitosamente",
                    content = @Content(schema = @Schema(implementation = ResumenDiarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Request inválida", content = @Content),
            @ApiResponse(responseCode = "503", description = "LLM no disponible", content = @Content)
    })
    ResponseEntity<ResumenDiarioResponse> resumenDiario(
            @Valid @RequestBody ResumenDiarioRequest request);
}
