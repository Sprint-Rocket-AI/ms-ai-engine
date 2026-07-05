package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperBean {

    @Bean
    ObjectMapper objectMapper (){
        return new ObjectMapper();
    }
}
