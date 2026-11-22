import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Минимальный JSON-парсер (без внешних библиотек).
 * Объекты -> Map, массивы -> List, числа -> Long/Double, строки -> String,
 * true/false -> Boolean, null -> null.
 * В отличие от разбора через split(",") корректно работает, если в описании
 * задачи встречаются запятые, двоеточия, кавычки и переносы строк.
 */
final class Json {

    private final String s;
    private int i;

    private Json(String s) {
        this.s = s;
    }

    static Object parse(String text) {
        Json p = new Json(text);
        p.skipWs();
        Object value = p.value();
        p.skipWs();
        if (p.i != p.s.length()) {
            throw p.error("Лишние символы после JSON");
        }
        return value;
    }

    /** Экранирует строку и берёт её в кавычки - для формирования тела запроса. */
    static String quote(String text) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : text.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.append('"').toString();
    }

    private RuntimeException error(String message) {
        return new IllegalArgumentException(message + " (позиция " + i + ")");
    }

    private char peek() {
        if (i >= s.length()) {
            throw error("Неожиданный конец JSON");
        }
        return s.charAt(i);
    }

    private void skipWs() {
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
    }

    private void expect(char c) {
        if (peek() != c) {
            throw error("Ожидался символ '" + c + "'");
        }
        i++;
    }

    private Object value() {
        char c = peek();
        if (c == '{') return object();
        if (c == '[') return array();
        if (c == '"') return string();
        if (s.startsWith("true", i)) { i += 4; return Boolean.TRUE; }
        if (s.startsWith("false", i)) { i += 5; return Boolean.FALSE; }
        if (s.startsWith("null", i)) { i += 4; return null; }
        return number();
    }

    private Map<String, Object> object() {
        Map<String, Object> map = new LinkedHashMap<>();
        expect('{');
        skipWs();
        if (peek() == '}') {
            i++;
            return map;
        }
        while (true) {
            skipWs();
            String key = string();
            skipWs();
            expect(':');
            skipWs();
            map.put(key, value());
            skipWs();
            char c = peek();
            i++;
            if (c == '}') return map;
            if (c != ',') throw error("Ожидалась ',' или '}'");
        }
    }

    private List<Object> array() {
        List<Object> list = new ArrayList<>();
        expect('[');
        skipWs();
        if (peek() == ']') {
            i++;
            return list;
        }
        while (true) {
            skipWs();
            list.add(value());
            skipWs();
            char c = peek();
            i++;
            if (c == ']') return list;
            if (c != ',') throw error("Ожидалась ',' или ']'");
        }
    }

    private String string() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = peek();
            i++;
            if (c == '"') return sb.toString();
            if (c != '\\') {
                sb.append(c);
                continue;
            }
            char e = peek();
            i++;
            switch (e) {
                case 'n' -> sb.append('\n');
                case 't' -> sb.append('\t');
                case 'r' -> sb.append('\r');
                case 'b' -> sb.append('\b');
                case 'f' -> sb.append('\f');
                case 'u' -> {
                    if (i + 4 > s.length()) throw error("Неверная escape-последовательность");
                    sb.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
                    i += 4;
                }
                default -> sb.append(e); // " \ /
            }
        }
    }

    private Object number() {
        int start = i;
        while (i < s.length() && "+-0123456789.eE".indexOf(s.charAt(i)) >= 0) {
            i++;
        }
        if (start == i) throw error("Неожиданный символ '" + s.charAt(i) + "'");
        String t = s.substring(start, i);
        return (t.contains(".") || t.contains("e") || t.contains("E")) ? (Object) Double.valueOf(t) : (Object) Long.valueOf(t);
    }
}
