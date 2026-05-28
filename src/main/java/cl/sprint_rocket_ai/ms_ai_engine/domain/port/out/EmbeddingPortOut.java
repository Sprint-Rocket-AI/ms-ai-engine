package cl.sprint_rocket_ai.ms_ai_engine.domain.port.out;

import java.util.List;

public interface EmbeddingPortOut {
    List<float[]> embed(List<String> textos);
}

