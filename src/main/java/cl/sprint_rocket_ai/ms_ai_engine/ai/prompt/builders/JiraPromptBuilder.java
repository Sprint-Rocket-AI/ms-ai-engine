package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import org.springframework.stereotype.Component;

@Component
public class JiraPromptBuilder {
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
