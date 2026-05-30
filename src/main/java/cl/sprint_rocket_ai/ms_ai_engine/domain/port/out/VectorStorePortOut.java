package cl.sprint_rocket_ai.ms_ai_engine.domain.port.out;

import cl.sprint_rocket_ai.ms_ai_engine.domain.model.VectorDocument;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.AIIndexRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VectorStorePortOut {
    void save(AIIndexRequest request);
    List<VectorDocument> search(String query);
    void savePdf(MultipartFile file);
}

