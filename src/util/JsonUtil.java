package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonUtil {
    private JsonUtil() {}

    private static String findString(String json, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"(.*?)\"");
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : null;
    }

    private static String findRaw(String json, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*([^,}\\s]+)");
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : null;
    }

    public static String str(String json, String key) {
        return findString(json, key);
    }

    public static Integer intOrNull(String json, String key) {
        String raw = findRaw(json, key);
        if (raw == null) return null;
        if ("null".equalsIgnoreCase(raw)) return null;
        return Integer.parseInt(raw);
    }

    public static Double doubleOrNull(String json, String key) {
        String raw = findRaw(json, key);
        if (raw == null) return null;
        if ("null".equalsIgnoreCase(raw)) return null;
        return Double.parseDouble(raw);
    }

    public static Boolean boolOrNull(String json, String key) {
        String raw = findRaw(json, key);
        if (raw == null) return null;
        if ("null".equalsIgnoreCase(raw)) return null;
        if ("true".equalsIgnoreCase(raw)) return true;
        if ("false".equalsIgnoreCase(raw)) return false;
        return null;
    }

    public static String ok(String msg) {
        return "{\"status\":\"ok\",\"message\":\"" + escape(msg) + "\"}";
    }

    public static String err(String msg) {
        return "{\"status\":\"error\",\"message\":\"" + escape(msg) + "\"}";
    }

    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
