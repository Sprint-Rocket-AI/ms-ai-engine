package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.in.rest.dtos;

import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.SystemPromptTypeEnum;

public record PromptRequest(
        SystemPromptTypeEnum type,
        String userInput,
        String context
) {
}
