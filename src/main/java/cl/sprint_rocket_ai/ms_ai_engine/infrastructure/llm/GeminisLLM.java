package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.llm;

import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import org.springframework.stereotype.Component;

@Component
public class GeminisLLM {

    public String generate(String prompt) {
        return "[Gemini simulated] " + prompt;
    }

    public String embedding (){
        return "";
    }
}

