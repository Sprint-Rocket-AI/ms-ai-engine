package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.ai.agents.DailySummaryAgent;
import cl.sprint_rocket_ai.ms_ai_engine.ai.agents.SuggestActivitiesAgent;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.CheckpointController;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.ResumenDiarioRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.ResumenDiarioResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.actividad.SugerirActividadesRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.actividad.SugerirActividadesResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkpoint")
public class CheckpointRestController implements CheckpointController {

    private final SuggestActivitiesAgent suggestActivitiesAgent;
    private final DailySummaryAgent dailySummaryAgent;

    public CheckpointRestController(SuggestActivitiesAgent suggestActivitiesAgent,
                                    DailySummaryAgent dailySummaryAgent) {
        this.suggestActivitiesAgent = suggestActivitiesAgent;
        this.dailySummaryAgent = dailySummaryAgent;
    }

    @Override
    @PostMapping("/sugerir-actividades")
    public ResponseEntity<SugerirActividadesResponse> sugerirActividades(
            @Valid @RequestBody SugerirActividadesRequest request) {
        return ResponseEntity.ok(suggestActivitiesAgent.suggest(request));
    }

    @Override
    @PostMapping("/resumen-diario")
    public ResponseEntity<ResumenDiarioResponse> resumenDiario(
            @Valid @RequestBody ResumenDiarioRequest request) {
        return ResponseEntity.ok(dailySummaryAgent.dailySummary(request));
    }
}
