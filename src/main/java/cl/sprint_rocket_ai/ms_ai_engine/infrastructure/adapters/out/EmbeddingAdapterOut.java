package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.out;

import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.EmbeddingPortOut;
import cl.sprint_rocket_ai.ms_ai_engine.infrastructure.llm.GeminisLLM;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmbeddingAdapterOut implements EmbeddingPortOut {

    private final GeminisLLM geminisLLM;

    public EmbeddingAdapterOut(GeminisLLM geminisLLM) {
        this.geminisLLM = geminisLLM;
    }

    @Override
    public List<float[]> embed(List<String> textos) {
        return geminisLLM.embedding(textos);
    }
}
