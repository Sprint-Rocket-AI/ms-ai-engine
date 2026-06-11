package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai.config.semantic_cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SemanticCacheAdvisor {

    private static final Logger log = LoggerFactory.getLogger(SemanticCacheAdvisor.class);

    private final VectorStore cacheStore;

    @Value("${semantic.cache.similarity-threshold}")
    private double similarityThreshold;

    public SemanticCacheAdvisor(@Qualifier("redisCacheVectorStore") VectorStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    public Optional<String> findInCache(String question) {
        log.info("Buscando existencia en caché");
        List<Document> hits = cacheStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(1)
                        .similarityThreshold(similarityThreshold)
                        .build()
        );
        log.info("Fin de búsqueda, registro en caché: {}",hits.size());
        return hits.stream()
                .findFirst()
                .map(doc -> (String) doc.getMetadata().get("answer"));
    }

    public void saveToCache(String question, String answer) {
        log.info("Iniciando guardado en semantic caché");
        Document documentToSave = Document.builder()
                .text(question)
                .metadata(Map.of("answer", answer))
                .build();
        cacheStore.add(List.of(documentToSave));
        log.info("Caché Guardado para la pregunta: {}", question);
    }
}