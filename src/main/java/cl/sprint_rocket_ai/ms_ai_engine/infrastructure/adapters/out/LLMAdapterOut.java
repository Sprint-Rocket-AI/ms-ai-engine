package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.out;

import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.llm.GeminisLLM;
import org.springframework.stereotype.Component;

@Component
public class LLMAdapterOut implements LLMPortOut {

    private final GeminisLLM geminisLLM;

    public LLMAdapterOut(GeminisLLM geminisLLM) {
        this.geminisLLM = geminisLLM;
    }

    @Override
    public String generate(String prompt) {
        return geminisLLM.generate(prompt);
    }
}
