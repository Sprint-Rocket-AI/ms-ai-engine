package cl.sprint_rocket_ai.ms_ai_engine.rest.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sugerencia de actividad generada por la IA")
public record SugerenciaItem(

        @Schema(description = "Título sugerido para la actividad", example = "Implementar refresh token")
        String titulo,

        @Schema(description = "Descripción detallada de la actividad sugerida",
                example = "Agregar soporte para renovación automática de tokens JWT")
        String descripcion,

        @Schema(description = "Prioridad recomendada", example = "ALTA")
        String prioridad,

        @Schema(description = "Justificación de la sugerencia",
                example = "Complementa la autenticación OAuth2 implementada para una experiencia completa")
        String razon
) {
}
