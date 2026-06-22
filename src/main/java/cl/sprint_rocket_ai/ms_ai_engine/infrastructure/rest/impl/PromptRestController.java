package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.ai.agents.MapperAgent;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.PromptController;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos.PromptMapperRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/prompt")
@CrossOrigin(origins = "*")
public class PromptRestController implements PromptController {
    private final MapperAgent mapperAgent;

    public PromptRestController(MapperAgent mapperAgent) {
        this.mapperAgent = mapperAgent;
    }

    @Override
    @PostMapping("/mapper")
    public ResponseEntity<Map<String,Object>> mapper(@Valid @RequestBody PromptMapperRequest request) {
        Map<String,Object> response = mapperAgent.map(request);
        return ResponseEntity.ok(response);
    }

}
