package cl.sprint_rocket_ai.ms_ai_engine.rest;

import cl.sprint_rocket_ai.ms_ai_engine.service.CheckpointService;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.ResumenDiarioRequest;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.ResumenDiarioResponse;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.SugerirActividadesRequest;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.SugerirActividadesResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkpoint")
public class CheckpointController implements CheckpointRest {

    private final CheckpointService checkpointService;

    public CheckpointController(CheckpointService checkpointService) {
        this.checkpointService = checkpointService;
    }

    @Override
    @PostMapping("/sugerir-actividades")
    public ResponseEntity<SugerirActividadesResponse> sugerirActividades(
            @Valid @RequestBody SugerirActividadesRequest request) {
        return ResponseEntity.ok(checkpointService.suggest(request));
    }

    @Override
    @PostMapping("/resumen-diario")
    public ResponseEntity<ResumenDiarioResponse> resumenDiario(
            @Valid @RequestBody ResumenDiarioRequest request) {
        return ResponseEntity.ok(checkpointService.dailySummary(request));
    }
}
