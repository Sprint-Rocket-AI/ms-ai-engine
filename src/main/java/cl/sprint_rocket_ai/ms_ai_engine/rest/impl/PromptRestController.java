package cl.sprint_rocket_ai.ms_ai_engine.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.service.PromptService;
import cl.sprint_rocket_ai.ms_ai_engine.rest.PromptController;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.PromptMapperRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/prompt")
@CrossOrigin(origins = "*")
public class PromptRestController implements PromptController {
    private final PromptService promptService;

    public PromptRestController(PromptService promptService) {
        this.promptService = promptService;
    }

    @Override
    @PostMapping("/mapper")
    public ResponseEntity<Map<String,Object>> mapper(@Valid @RequestBody PromptMapperRequest request) {
        Map<String,Object> response = promptService.map(request);
        return ResponseEntity.ok(response);
    }

}
