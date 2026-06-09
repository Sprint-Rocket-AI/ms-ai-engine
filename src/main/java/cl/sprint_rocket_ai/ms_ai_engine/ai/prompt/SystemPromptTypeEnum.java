package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt;

public enum SystemPromptTypeEnum {
    MAPPER("system_prompts/MAPPER.txt"),
    RAG("system_prompts/RAG.txt"),
    CHECKPOINT_SUGGEST("system_prompts/CHECKPOINT_SUGGEST.txt"),
    CHECKPOINT_DAILY_SUMMARY("system_prompts/CHECKPOINT_DAILY_SUMMARY.txt")
    ;

    private final String path;

    SystemPromptTypeEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
