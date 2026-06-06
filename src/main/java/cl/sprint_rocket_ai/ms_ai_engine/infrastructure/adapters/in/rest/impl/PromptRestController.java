package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.application.service.PromptService;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.PromptController;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.AIResponse;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.PromptMapperRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prompt")
public class PromptRestController implements PromptController {
    private final PromptService promptService;

    public PromptRestController(PromptService promptService) {
        this.promptService = promptService;
    }

    @Override
    @PostMapping("/mapper")
    public ResponseEntity<AIResponse> mapper(@Valid @RequestBody PromptMapperRequest request) {
        String answer = promptService.mapper(request);
        return ResponseEntity.ok(new AIResponse(answer));
    }

}
