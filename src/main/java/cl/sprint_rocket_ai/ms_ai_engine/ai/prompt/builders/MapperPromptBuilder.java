package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.builders;

import cl.sprint_rocket_ai.ms_ai_engine.ai.prompt.PromptTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MapperPromptBuilder implements AbstractPromptBuilder{

    @Override
    public PromptTypeEnum getType() {
        return PromptTypeEnum.MAPPER;
    }

    private static final String TEMPLATE = """
            <structure>
            %s
            </structure>
            
            <content>
            %s
            </content>
            """;

    public String build(String userPrompt, Map<String, Object> template) {

        String structure = template.entrySet().stream()
                .map(e -> "- %s: %s".formatted(e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));

        return TEMPLATE.formatted(structure, userPrompt);
    }
}
