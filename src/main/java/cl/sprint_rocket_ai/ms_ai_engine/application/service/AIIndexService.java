package cl.sprint_rocket_ai.ms_ai_engine.application.service;

import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos.AIIndexRequest;
import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.VectorStorePortOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class AIIndexService {

    private static final Logger log = LoggerFactory.getLogger(AIIndexService.class);
    private final VectorStorePortOut vectorStorePortOut;


    public AIIndexService(VectorStorePortOut vectorStorePortOut) {
        this.vectorStorePortOut = vectorStorePortOut;
    }

    public void index(AIIndexRequest request) {
        log.info("Iniciando indexación");
        vectorStorePortOut.save(request);
        log.info("Fin de la indexación");
    }


}
