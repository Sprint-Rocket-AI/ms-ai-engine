package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class JiraPromptBuilder implements AbstractPromptBuilder{

    @Override
    public PromptTypeEnum getType() {
        return PromptTypeEnum.JIRA_TOOL;
    }

    private static final String TEMPLATE = """
        <context>
        userId:%s
        </context>
        
        <question>
        %s
        </question>
        """;

    public String build(String userPrompt, String userId) {
        return TEMPLATE.formatted(userId, userPrompt);
    }

}
