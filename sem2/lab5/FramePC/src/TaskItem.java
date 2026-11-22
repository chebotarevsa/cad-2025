import java.time.LocalDate;
import java.util.Map;

/** Задача, полученная от сервера (поля соответствуют классу Task на сервере). */
record TaskItem(long id, String title, String description, LocalDate dueDate, boolean completed) {

    static TaskItem fromJson(Map<?, ?> json) {
        Object id = json.get("id");
        Object title = json.get("title");
        Object description = json.get("description");
        Object dueDate = json.get("dueDate");
        Object completed = json.get("completed");
        return new TaskItem(
                id instanceof Number n ? n.longValue() : 0L,
                title == null ? "" : title.toString(),
                description == null ? "" : description.toString(),
                dueDate == null ? null : LocalDate.parse(dueDate.toString()),
                Boolean.TRUE.equals(completed));
    }
}
