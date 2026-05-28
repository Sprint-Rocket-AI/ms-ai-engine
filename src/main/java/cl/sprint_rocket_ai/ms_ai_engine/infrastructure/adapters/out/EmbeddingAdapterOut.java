package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.adapters.out;

import cl.sprint_rocket_ai.ms_ai_engine.domain.port.out.EmbeddingPortOut;

import java.util.List;

public class EmbeddingAdapterOut implements EmbeddingPortOut {
    @Override
    public List<float[]> embed(List<String> textos) {
        return List.of();
    }
}
