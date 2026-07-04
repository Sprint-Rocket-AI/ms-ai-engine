package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.vector_store;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.mongodb.atlas.MongoDBAtlasVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import redis.clients.jedis.RedisClient;

@Configuration
public class VectorStoreConfig {

    @Value("${semantic.cache.redis.host}")
    private String cacheRedisHost;

    @Value("${semantic.cache.redis.port}")
    private int cacheRedisPort;

    @Value("${semantic.cache.redis.user}")
    private String cacheRedisUser;

    @Value("${semantic.cache.redis.password}")
    private String cacheRedisPassword;

    @Bean(name = "mongoAtlasVectorStore")
    @Primary
    public VectorStore mongoAtlasVectorStore(MongoTemplate mongoTemplate, EmbeddingModel embeddingModel) {
        String vectorCollectionName = "vector_store";
        String vectorIndexName = "vector_index";
        String vectorPathName = "embedding";

        return MongoDBAtlasVectorStore.builder(mongoTemplate, embeddingModel)
                .collectionName(vectorCollectionName)
                .vectorIndexName(vectorIndexName)
                .pathName(vectorPathName)
                .initializeSchema(false)
                .build();
    }

    @Bean(name = "redisCacheVectorStore")
    public VectorStore redisCacheVectorStore(EmbeddingModel embeddingModel) {
        RedisClient redisClient = RedisClient.create(cacheRedisHost, cacheRedisPort, cacheRedisUser, cacheRedisPassword);
        String indexName = "cache-index";
        String cachePrefix = "cache:";
        return RedisVectorStore.builder(redisClient, embeddingModel)
                .indexName(indexName)
                .prefix(cachePrefix)
                .metadataFields(RedisVectorStore.MetadataField.tag("answer"))
                .initializeSchema(true)
                .build();
    }
}