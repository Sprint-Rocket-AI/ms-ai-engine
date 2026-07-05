package cl.sprint_rocket_ai.ms_ai_engine.ai.prompt;

public enum PromptTypeEnum {
    MAPPER("system_prompts/MAPPER.txt"),
    RAG("system_prompts/RAG.txt"),
    CHECKPOINT_SUGGEST("system_prompts/CHECKPOINT_SUGGEST.txt"),
    CHECKPOINT_DAILY_SUMMARY("system_prompts/CHECKPOINT_DAILY_SUMMARY.txt"),
    ACTIVIDAD_TOOL("system_prompts/ACTIVIDAD_TOOL.txt"),
    RECORDATORIO_TOOL("system_prompts/RECORDATORIO_TOOL.txt"),
    JIRA_TOOL("system_prompts/JIRA_TOOL.txt")
    ;

    private final String pathSystemPrompt;

    PromptTypeEnum(String pathSystemPrompt) {
        this.pathSystemPrompt = pathSystemPrompt;
    }

    public String getPathSystemPrompt() {
        return pathSystemPrompt;
    }
}
