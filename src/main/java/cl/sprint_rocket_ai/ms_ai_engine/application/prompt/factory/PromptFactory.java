package cl.sprint_rocket_ai.ms_ai_engine.application.prompt.factory;

import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptBuilder;
import cl.sprint_rocket_ai.ms_ai_engine.application.prompt.PromptType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PromptFactory {

    private final Map<PromptType, PromptBuilder> registry = new EnumMap<>(PromptType.class);

    public PromptFactory(List<PromptBuilder> builders) {
        builders.forEach(b -> registry.put(b.getType(), b));
    }

    public PromptBuilder getBuilder(PromptType type) {
        PromptBuilder builder = registry.get(type);
        if (builder == null) {
            throw new IllegalArgumentException(
                    "No existe prompt para el tipo: " + type);
        }
        return builder;
    }
}