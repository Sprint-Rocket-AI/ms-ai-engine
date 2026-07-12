package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.ai.agents.DailySummaryAgent;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.CheckpointController;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.ResumenDiarioRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.ResumenDiarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkpoint")
public class CheckpointRestController implements CheckpointController {

    private final DailySummaryAgent dailySummaryAgent;

    public CheckpointRestController(DailySummaryAgent dailySummaryAgent) {
        this.dailySummaryAgent = dailySummaryAgent;
    }


    @Override
    @PostMapping("/resumen-diario")
    public ResponseEntity<ResumenDiarioResponse> resumenDiario(
            @Valid @RequestBody ResumenDiarioRequest request) {
        return ResponseEntity.ok(dailySummaryAgent.dailySummary(request));
    }
}
