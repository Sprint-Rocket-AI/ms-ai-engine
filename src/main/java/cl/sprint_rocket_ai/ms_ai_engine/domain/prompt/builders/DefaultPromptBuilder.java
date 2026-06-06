package cl.sprint_rocket_ai.ms_ai_engine.domain.prompt.builders;

import org.springframework.stereotype.Component;

@Component
public class DefaultPromptBuilder {

    private static final String TEMPLATE_WITH_CONTEXT = """
        <context>
        %s
        </context>
        
        <question>
        %s
        </question>
        """;

    private static final String TEMPLATE_WITHOUT_CONTEXT = """
        <question>
        %s
        </question>
        """;


    public String buildWithContext(String userPrompt, String context) {
        return TEMPLATE_WITH_CONTEXT.formatted(context, userPrompt);
    }

    public String build(String userPrompt) {
        return TEMPLATE_WITHOUT_CONTEXT.formatted(userPrompt);
    }
}
