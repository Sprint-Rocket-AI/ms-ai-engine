package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.out;

import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.LLMPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.llm.GeminisLLM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LLMAdapterOut implements LLMPortOut {

    private static final Logger log = LoggerFactory.getLogger(LLMAdapterOut.class);
    private final GeminisLLM geminisLLM;

    public LLMAdapterOut(GeminisLLM geminisLLM) {
        this.geminisLLM = geminisLLM;
    }

    @Override
    public String generate(String sessionId,String systemPrompt, String userPrompt) {
        log.info("Generando respuesta desde Geminis");
        return geminisLLM.generate(sessionId,systemPrompt,userPrompt);
    }
}
