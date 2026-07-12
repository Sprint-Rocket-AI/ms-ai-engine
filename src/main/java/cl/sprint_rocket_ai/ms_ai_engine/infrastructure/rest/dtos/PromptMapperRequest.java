package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.rest.dtos;

import java.util.Map;

public record PromptMapperRequest(
        String content,
        Map<String,Object> template
) {
}
