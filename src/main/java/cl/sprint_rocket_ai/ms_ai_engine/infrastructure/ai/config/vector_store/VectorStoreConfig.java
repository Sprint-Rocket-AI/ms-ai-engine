package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai.config.vector_store;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPooled;

@Configuration
public class VectorStoreConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean(name = "pgVectorStore")
    @Primary
    public VectorStore pgVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName("ai_embeddings")
                .dimensions(768)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .initializeSchema(true)
                .maxDocumentBatchSize(10000)
                .build();
    }

    @Bean(name = "redisVectorStore")
    public VectorStore redisVectorStore(EmbeddingModel embeddingModel) {
        JedisPooled jedis = new JedisPooled(redisHost, redisPort);

        return RedisVectorStore.builder(jedis, embeddingModel)
                .indexName("my-index")
                .prefix("doc:")
                .initializeSchema(true)
                .build();
    }
}