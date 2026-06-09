package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuración de Jackson para ms-ai-engine.
 *
 * <p>Registra {@link ObjectMapper} como bean de Spring para que pueda ser
 * inyectado en servicios como {@code CheckpointService}.
 * Spring Boot 4.x requiere configuración explícita cuando el autoconfigure
 * de Jackson no está activo o no registra el mapper en el contexto.
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Soporte para tipos de fecha/hora de java.time (LocalDate, LocalDateTime, etc.)
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // No falla si el JSON tiene campos desconocidos
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
