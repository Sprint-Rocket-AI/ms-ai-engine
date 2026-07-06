package cl.sprint_rocket_ai.ms_ai_engine;

import org.springframework.ai.model.google.genai.autoconfigure.chat.GoogleGenAiChatAutoConfiguration;
import org.springframework.ai.vectorstore.redis.autoconfigure.RedisVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        RedisVectorStoreAutoConfiguration.class,
        GoogleGenAiChatAutoConfiguration.class
})
public class MsAiEngineApplication {

     static void main(String[] args) {
        SpringApplication.run(MsAiEngineApplication.class, args);
    }

}
