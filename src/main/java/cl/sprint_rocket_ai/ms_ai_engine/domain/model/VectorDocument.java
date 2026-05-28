package cl.sprint_rocket_ai.ms_ai_engine.domain.model;

import java.util.Map;

public record VectorDocument(
        String id,
        String content,
        Map<String, Object> metadata
) {
}

