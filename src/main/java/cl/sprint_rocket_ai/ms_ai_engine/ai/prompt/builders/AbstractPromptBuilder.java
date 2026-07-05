package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;

public interface AbstractPromptBuilder {
    PromptTypeEnum getType();
}
