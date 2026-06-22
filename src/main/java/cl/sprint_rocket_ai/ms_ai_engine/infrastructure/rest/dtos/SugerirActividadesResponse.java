package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta con las 5 sugerencias de actividades generadas por la IA")
public record SugerirActividadesResponse(

        @Schema(description = "Lista de 5 actividades sugeridas para dar continuidad al trabajo")
        List<SugerenciaItem> sugerencias
) {
}
