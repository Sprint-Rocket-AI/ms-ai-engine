package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.VectorStorePortOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfIndexService {

    private static final Logger log = LoggerFactory.getLogger(PdfIndexService.class);
    private final VectorStorePortOut vectorStorePortOut;

    public PdfIndexService(VectorStorePortOut vectorStorePortOut) {
        this.vectorStorePortOut = vectorStorePortOut;
    }

    public void index(MultipartFile file){
        log.info("Inicio de indexacion de PDF id='{}'", file.getOriginalFilename());
        vectorStorePortOut.savePdf(file);
        log.info("Fin de indexacion de PDF id='{}'", file.getOriginalFilename());
    }
}
