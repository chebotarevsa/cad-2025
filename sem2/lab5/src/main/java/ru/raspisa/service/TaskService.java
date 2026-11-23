package ru.raspisa.service;

import jakarta.validation.constraints.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.raspisa.entity.*;
import ru.raspisa.repository.*;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {
    private final TaskRepository tasks;
    private final TaskHistoryRepository history;
    private final UserRepository users;

    public TaskService(TaskRepository tasks, TaskHistoryRepository history, UserRepository users) {
        this.tasks = tasks;
        this.history = history;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<TaskView> list() {
        return tasks.findByDeletedFalseOrderByCreatedAtDescIdDesc().stream().map(this::view).toList();
    }

    public TaskView create(TaskRequest request) {
        var task = new Task();
        task.owner = currentUser();
        apply(task, request);
        tasks.saveAndFlush(task);
        record(task, "CREATE", null);
        return view(task);
    }

    public TaskView update(long id, TaskRequest request) {
        var task = find(id);
        String before = snapshot(task);
        apply(task, request);
        record(task, "UPDATE", before);
        return view(task);
    }

    public void delete(long id) {
        var task = find(id);
        String before = snapshot(task);
        task.deleted = true;
        record(task, "DELETE", before);
    }

    public TaskView addSubtask(long id, SubtaskRequest request) {
        var task = find(id);
        String before = snapshot(task);
        var subtask = new SubTask();
        subtask.task = task;
        subtask.description = clean(request.description(), "Описание подзадачи");
        subtask.completed = request.completed();
        task.subtasks.add(subtask);
        tasks.saveAndFlush(task);
        record(task, "SUBTASK_ADD", before);
        return view(task);
    }

    public TaskView updateSubtask(long id, long subtaskId, SubtaskRequest request) {
        var task = find(id);
        String before = snapshot(task);
        var subtask = subtask(task, subtaskId);
        subtask.description = clean(request.description(), "Описание подзадачи");
        subtask.completed = request.completed();
        record(task, "SUBTASK_UPDATE", before);
        return view(task);
    }

    public TaskView deleteSubtask(long id, long subtaskId) {
        var task = find(id);
        String before = snapshot(task);
        task.subtasks.remove(subtask(task, subtaskId));
        record(task, "SUBTASK_DELETE", before);
        return view(task);
    }

    @Transactional(readOnly = true)
    public List<HistoryView> history(long id) {
        if (!tasks.existsById(id)) throw new NoSuchElementException("Задача не найдена");
        return history.findByTaskIdOrderByChangedAtDescIdDesc(id).stream()
                .map(h -> new HistoryView(h.id, h.changedAt, h.action, h.actor.getLogin(), h.oldState, h.newState)).toList();
    }

    private Task find(long id) {
        return tasks.findById(id).filter(t -> !t.deleted)
                .orElseThrow(() -> new NoSuchElementException("Задача не найдена"));
    }

    private SubTask subtask(Task task, long id) {
        return task.subtasks.stream().filter(s -> s.id.equals(id)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Подзадача не найдена в этой задаче"));
    }

    private void apply(Task task, TaskRequest request) {
        task.title = clean(request.title(), "Название");
        task.description = request.description() == null ? "" : request.description().strip();
        task.category = clean(request.category(), "Категория");
        task.priority = request.priority();
        task.status = request.status();
    }

    private String clean(String value, String label) {
        String result = value == null ? "" : value.strip();
        if (result.isEmpty()) throw new IllegalArgumentException(label + " не может быть пустым");
        return result;
    }

    private User currentUser() {
        var user = users.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) throw new NoSuchElementException("Пользователь не найден");
        return user;
    }

    private void record(Task task, String action, String before) {
        String after = snapshot(task);
        if (Objects.equals(before, after)) return;
        var entry = new TaskHistory();
        entry.task = task;
        entry.actor = currentUser();
        entry.action = action;
        entry.oldState = before;
        entry.newState = after;
        history.save(entry);
    }

    private String snapshot(Task task) {
        return "Название: " + task.title + "\nОписание: " + task.description
                + "\nСтатус: " + task.status + "\nПриоритет: " + task.priority
                + "\nКатегория: " + task.category + "\nАвтор: " + task.owner.getLogin()
                + "\nУдалена: " + task.deleted + "\nПодзадачи:\n"
                + task.subtasks.stream().map(s -> "#" + s.id + " [" + (s.completed ? "+" : " ") + "] " + s.description)
                        .collect(Collectors.joining("\n"));
    }

    private TaskView view(Task task) {
        return new TaskView(task.id, task.title, task.description, task.createdAt, task.priority, task.category,
                task.status, task.owner.getLogin(), task.subtasks.stream()
                        .map(s -> new SubtaskView(s.id, s.description, s.completed)).toList());
    }

    public record TaskRequest(@NotBlank @Size(max = 200) String title, @Size(max = 10000) String description,
            @NotNull Task.Priority priority, @NotBlank @Size(max = 100) String category, @NotNull Task.Status status) {}
    public record SubtaskRequest(@NotBlank @Size(max = 500) String description, boolean completed) {}
    public record SubtaskView(Long id, String description, boolean completed) {}
    public record TaskView(Long id, String title, String description, Instant createdAt, Task.Priority priority,
            String category, Task.Status status, String owner, List<SubtaskView> subtasks) {}
    public record HistoryView(Long id, Instant changedAt, String action, String actor, String oldState, String newState) {}
}
