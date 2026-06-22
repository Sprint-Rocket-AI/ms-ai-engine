package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.actividad;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Solicitud de sugerencia de actividades para dar continuidad al trabajo del desarrollador")
public record SugerirActividadesRequest(

        @NotBlank
        @Schema(description = "Identificador del desarrollador", example = "dev-matias-001")
        String userId,

        @NotNull
        @Schema(description = "Fecha de referencia de las actividades", example = "2026-06-05")
        LocalDate fecha,

        @NotEmpty
        @Schema(description = "Lista de actividades del día para analizar")
        List<ActividadItem> actividades
) {
}
