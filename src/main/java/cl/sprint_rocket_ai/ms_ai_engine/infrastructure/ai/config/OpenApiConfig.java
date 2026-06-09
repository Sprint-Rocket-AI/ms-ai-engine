package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.ai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ms-ai-engine API")
                        .description("Microservicio de motor de IA para procesamiento de lenguaje natural y embeddings")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SprintRocket AI")
                                .email("soporte@sprintrocket.ai"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://sprintrocket.ai")));
    }
}
