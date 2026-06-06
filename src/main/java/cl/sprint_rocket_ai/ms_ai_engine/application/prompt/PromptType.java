package cl.sprint_rocket_ai.ms_ai_engine.application.prompt;

/**
 * Identifica el módulo que origina la interacción con la IA.
 * Actúa como discriminador de estrategia en {@code PromptFactory}.
 *
 * El {@code moduleAlias} es el valor que llega en el campo {@code module}
 * del {@code RAGRequest} (ej. "devmentor", "checkpoint", "roadmap").
 */
public enum PromptType {

    DEV_MENTOR("devmentor"),
    CHECKPOINT("checkpoint"),
    ROADMAP("roadmap");

    private final String moduleAlias;

    PromptType(String moduleAlias) {
        this.moduleAlias = moduleAlias;
    }

    public String getModuleAlias() {
        return moduleAlias;
    }

    /**
     * Resuelve el {@code PromptType} a partir del alias de módulo recibido en el request.
     *
     * @param module alias del módulo (case-insensitive), p.ej. "devmentor"
     * @return el {@code PromptType} correspondiente
     * @throws IllegalArgumentException si el módulo no está registrado
     */
    public static PromptType fromString(String module) {
        if (module == null) {
            throw new IllegalArgumentException("El campo 'module' no puede ser nulo");
        }
        String normalized = module.trim().toLowerCase();
        for (PromptType type : values()) {
            if (type.moduleAlias.equals(normalized)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "Módulo desconocido: '" + module + "'. Módulos válidos: devmentor, checkpoint, roadmap");
    }
}
