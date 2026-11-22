import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Все обращения к серверу собраны здесь, формы работают только через этот класс. */
final class ApiClient {

    /** Ошибка обращения к серверу с понятным пользователю сообщением. */
    static final class ApiException extends Exception {
        final int status;

        ApiException(int status, String message) {
            super(message);
            this.status = status;
        }
    }

    /** Данные о текущем пользователе (ответ /api/me). */
    record UserInfo(String username, List<String> roles) {
        boolean hasRole(String role) {
            return roles.contains(role);
        }
    }

    static final String BASE_URL = System.getProperty("api.url", "http://localhost:8080/api");

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final String authHeader;

    /** @param encodedAuth строка Base64 от "login:password" */
    ApiClient(String encodedAuth) {
        this.authHeader = "Basic " + encodedAuth;
    }

    static String encodeCredentials(String username, String password) {
        return java.util.Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    UserInfo me() throws ApiException {
        Map<?, ?> json = (Map<?, ?>) parse(send(builder("/me").GET()).body());
        List<String> roles = new ArrayList<>();
        if (json.get("roles") instanceof List<?> list) {
            list.forEach(r -> roles.add(String.valueOf(r)));
        }
        return new UserInfo(String.valueOf(json.get("username")), roles);
    }

    List<TaskItem> getTasks() throws ApiException {
        List<TaskItem> result = new ArrayList<>();
        Object json = parse(send(builder("").GET()).body());
        if (json instanceof List<?> list) {
            for (Object o : list) {
                result.add(TaskItem.fromJson((Map<?, ?>) o));
            }
        }
        return result;
    }

    void addTask(String title, String description, LocalDate dueDate) throws ApiException {
        String body = "{\"title\":" + Json.quote(title)
                + ",\"description\":" + Json.quote(description == null ? "" : description)
                + ",\"dueDate\":" + (dueDate == null ? "null" : Json.quote(dueDate.toString())) + "}";
        send(builder("").header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8)));
    }

    void setCompleted(long id, boolean completed) throws ApiException {
        send(builder("/" + id + "?completed=" + completed).PUT(HttpRequest.BodyPublishers.noBody()));
    }

    void deleteTask(long id) throws ApiException {
        send(builder("/" + id).DELETE());
    }

    // ---------------------------------------------------------------

    private HttpRequest.Builder builder(String path) {
        return HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10))
                .header("Authorization", authHeader)
                // Accept: JSON нужен, чтобы Spring Security отвечал клиенту кодом 401,
                // а не перенаправлял на HTML-страницу логина
                .header("Accept", "application/json");
    }

    private HttpResponse<String> send(HttpRequest.Builder request) throws ApiException {
        try {
            HttpResponse<String> response = http.send(request.build(),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            int code = response.statusCode();
            if (code >= 200 && code < 300) {
                return response;
            }
            switch (code) {
                case 401 -> throw new ApiException(code, "Неверное имя пользователя или пароль");
                case 403 -> throw new ApiException(code, "Недостаточно прав для этого действия");
                case 400 -> throw new ApiException(code, "Сервер отклонил запрос: проверьте введённые данные");
                default -> throw new ApiException(code, "Ошибка сервера (код " + code + ")");
            }
        } catch (IOException e) {
            throw new ApiException(0, "Не удалось подключиться к серверу " + BASE_URL
                    + ". Проверьте, что сервер запущен.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(0, "Запрос прерван");
        }
    }

    private static Object parse(String body) throws ApiException {
        try {
            return Json.parse(body);
        } catch (IllegalArgumentException e) {
            throw new ApiException(0, "Сервер вернул некорректный ответ: " + e.getMessage());
        }
    }
}
