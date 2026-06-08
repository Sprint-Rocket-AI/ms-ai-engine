package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.semantic_cache;

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

    public SemanticCacheAdvisor(@Qualifier("redisVectorStore") VectorStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    public Optional<String> findInCache(String question) {
        List<Document> hits = cacheStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(1)
                        .similarityThreshold(similarityThreshold)
                        .build()
        );
        return hits.stream().findFirst().map(Document::getText);
    }

    public void saveToCache(String question, String answer) {
        cacheStore.add(List.of(
                Document.builder()
                        .text(answer)
                        .metadata(Map.of("question", question))
                        .build()
        ));
        log.debug("Cache Guardado para la pregunta: {}", question);
    }
}