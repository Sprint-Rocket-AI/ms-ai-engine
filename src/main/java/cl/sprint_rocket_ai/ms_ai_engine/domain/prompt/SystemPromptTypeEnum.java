package cl.sprint_rocket_ai.ms_ai_engine.domain.prompt;

public enum SystemPromptTypeEnum {
    MAPPER("system_prompts/MAPPER.txt"),
    RAG("system_prompts/RAG.txt")
    ;

    private final String path;

    SystemPromptTypeEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
