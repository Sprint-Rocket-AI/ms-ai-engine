package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest;

import cl.sprint_rocket_ai.ms_ai_engine.application.service.AIIndexService;
import cl.sprint_rocket_ai.ms_ai_engine.application.service.RAGService;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.AIIndexRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AIController implements AIRest {
    private final RAGService ragService;
    private final AIIndexService aiIndexService;

    public AIController(RAGService ragService, AIIndexService aiIndexService) {
        this.ragService = ragService;
        this.aiIndexService = aiIndexService;
    }

    @Override
    @PostMapping("/index")
    public ResponseEntity<Void> index(@Valid @RequestBody AIIndexRequest request) {
        aiIndexService.index(request);
        return ResponseEntity.accepted().build();
    }

    @Override
    @PostMapping("/rag")
    public ResponseEntity<RAGResponse> rag(@Valid @RequestBody RAGRequest request) {
        String answer = ragService.ask(request);
        return ResponseEntity.ok(new RAGResponse(answer));
    }
}
