package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta del resumen diario ejecutivo generado por la IA")
public record ResumenDiarioResponse(

        @Schema(description = "Resumen ejecutivo del día con logros, bloqueadores y observaciones",
                example = "Durante el día se completaron 4 actividades de backend. Se detectó un bloqueador en el módulo de autenticación...")
        String resumen,

        @Schema(description = "Lista de actividades sugeridas para el día siguiente")
        List<SugerenciaItem> sugerencias
) {
}
