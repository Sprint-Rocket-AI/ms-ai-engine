package cl.sprint_rocket_ai.ms_ai_engine.rest.impl;

import cl.sprint_rocket_ai.ms_ai_engine.service.RAGService;
import cl.sprint_rocket_ai.ms_ai_engine.service.VectorStoreService;
import cl.sprint_rocket_ai.ms_ai_engine.rest.RAGController;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.AIIndexRequest;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.AIRequest;
import cl.sprint_rocket_ai.ms_ai_engine.rest.dtos.AIResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/rag")
public class RAGRestController implements RAGController {
    private final RAGService ragService;
    private final VectorStoreService vectorStoreService;

    public RAGRestController(RAGService ragService, VectorStoreService vectorStoreService) {
        this.ragService = ragService;
        this.vectorStoreService = vectorStoreService;
    }

    @Override
    @PostMapping("/index")
    public ResponseEntity<Void> index(@Valid @RequestBody AIIndexRequest request) {
        vectorStoreService.save(request);
        return ResponseEntity.accepted().build();
    }

    @Override
    @PostMapping("/query")
    public ResponseEntity<AIResponse> rag(@Valid @RequestBody AIRequest request) {
        String answer = ragService.ask(request);
        return ResponseEntity.ok(new AIResponse(answer));
    }

    @Override
    @PostMapping(value = "/index/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> indexPdf(@RequestPart("file") MultipartFile file) {
        vectorStoreService.savePdf(file);
        return ResponseEntity.accepted().build();
    }
}
