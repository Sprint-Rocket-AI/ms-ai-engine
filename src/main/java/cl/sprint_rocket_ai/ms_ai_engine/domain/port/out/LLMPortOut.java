package cl.sprint_rocket_ai.ms_ai_engine.domain.port.out;

public interface LLMPortOut {
    String generate(String systemPrompt, String userPrompt);
}

