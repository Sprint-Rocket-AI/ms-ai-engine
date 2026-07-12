package cl.sprint_rocket_ai.ms_ai_engine.infrastructure.config.context_filter;

public final class UserContextHolder {

    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    private UserContextHolder() {}

    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    public static String getUserId() {
        return USER_ID.get();
    }

    public static void clear() {
        USER_ID.remove();
    }
}