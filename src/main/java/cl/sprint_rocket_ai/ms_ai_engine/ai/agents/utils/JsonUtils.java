package cl.sprint_rocket_ai.ms_ai_engine.ai.agents.utils;

public class JsonUtils {
    private JsonUtils() {
    }

    public static String extractJson(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');

        if (start == -1 || end == -1 || end < start) {
            throw new IllegalArgumentException("No se encontró un JSON válido");
        }

        return text.substring(start, end + 1);
    }
}
