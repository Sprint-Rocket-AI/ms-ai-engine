package cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class SystemPromptLoaderUtils {

    private final ResourceLoader resourceLoader;

    public SystemPromptLoaderUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String load(String path) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + path);

            try (InputStream is = resource.getInputStream()) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading prompt: " + path, e);
        }
    }
}
