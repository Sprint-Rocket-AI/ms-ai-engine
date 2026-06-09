package cl.sprint_rocket_ai.ms_ai_engine.rest.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Actividad enviada desde ms-checkpoint para análisis de IA")
public record ActividadItem(

        @Schema(description = "Título de la actividad", example = "Implementar autenticación OAuth2")
        String titulo,

        @Schema(description = "Descripción detallada", example = "Configurar flujo de autorización con provider externo")
        String descripcion,

        @Schema(description = "Estado de la actividad", example = "COMPLETADA")
        String estado,

        @Schema(description = "Prioridad asignada", example = "ALTA")
        String prioridad,

        @Schema(description = "Ticket Jira vinculado", example = "SPRINT-1234")
        String ticketJira,

        @Schema(description = "Notas adicionales del desarrollador", example = "Pendiente revisión de seguridad")
        String notas,

        @Schema(description = "Horas reales invertidas", example = "3.5")
        Double horasReales
) {
}
