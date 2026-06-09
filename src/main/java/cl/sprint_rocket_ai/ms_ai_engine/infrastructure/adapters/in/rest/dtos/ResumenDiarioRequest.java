package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Solicitud de resumen diario ejecutivo enviada por el scheduler de ms-checkpoint")
public record ResumenDiarioRequest(

        @NotBlank
        @Schema(description = "Identificador del desarrollador", example = "dev-matias-001")
        String userId,

        @NotNull
        @Schema(description = "Fecha del día analizado", example = "2026-06-05")
        LocalDate fecha,

        @NotEmpty
        @Schema(description = "Todas las actividades del día (pendientes y completadas)")
        List<ActividadItem> actividades
) {
}
