package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.vector_store;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.RedisClient;

@Configuration
public class VectorStoreConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${semantic.cache.index:cache-index}")
    private String cacheIndex;

    @Value("${semantic.cache.prefix:cache:}")
    private String cachePrefix;

    @Bean(name = "pgVectorStore")
    @Primary
    public VectorStore pgVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName("ai_embeddings")
                .dimensions(1024)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .initializeSchema(true)
                .idType(PgVectorStore.PgIdType.TEXT)
                .maxDocumentBatchSize(10000)
                .build();
    }

    @Bean(name = "redisCacheVectorStore")
    public VectorStore redisCacheVectorStore(EmbeddingModel embeddingModel) {
        RedisClient redisClient = RedisClient.create(redisHost, redisPort);
        return RedisVectorStore.builder(redisClient, embeddingModel)
                .indexName(cacheIndex)
                .prefix(cachePrefix)
                //Tag = Similarity 0.95, solo recuperas el answer
                //Text = Filtrar o buscas dentro del answer
                .metadataFields(RedisVectorStore.MetadataField.tag("answer"))
                .initializeSchema(true)
                .build();
    }
}