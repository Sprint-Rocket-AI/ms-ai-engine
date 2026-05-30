package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest;

import cl.sprint_rocket_ai.ms_ai_engine.application.service.AIIndexService;
import cl.sprint_rocket_ai.ms_ai_engine.application.service.PdfIndexService;
import cl.sprint_rocket_ai.ms_ai_engine.application.service.RAGService;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.AIIndexRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGRequest;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.RAGResponse;
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
@RequestMapping("/api")
public class AIController implements AIRest {
    private final RAGService ragService;
    private final AIIndexService aiIndexService;
    private final PdfIndexService pdfIndexService;

    public AIController(RAGService ragService, AIIndexService aiIndexService, PdfIndexService pdfIndexService) {
        this.ragService = ragService;
        this.aiIndexService = aiIndexService;
        this.pdfIndexService = pdfIndexService;
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

    @Override
    @PostMapping(value = "/index/pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> indexPdf(@RequestPart("file") MultipartFile file) {
        pdfIndexService.index(file);
        return ResponseEntity.accepted().build();
    }
}
