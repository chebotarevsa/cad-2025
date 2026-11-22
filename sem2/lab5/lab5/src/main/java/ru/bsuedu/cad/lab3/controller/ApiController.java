package ru.bsuedu.cad.lab3.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ru.bsuedu.cad.lab3.model.Priority;
import ru.bsuedu.cad.lab3.model.SubTask;
import ru.bsuedu.cad.lab3.model.Task;
import ru.bsuedu.cad.lab3.service.TaskService;

/**
 * REST-интерфейс для клиентского (Swing) приложения.
 * Веб-интерфейс на Thymeleaf (TaskController) остаётся без изменений.
 * Сущности JPA напрямую в JSON не отдаём (ленивые связи и циклическая ссылка Task <-> SubTask),
 * вместо них используются простые DTO-записи.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    public record SubTaskDto(Long id, String description, boolean completed) {
    }

    public record TaskDto(Long id, String title, String description, boolean completed,
                          LocalDate dueDate, LocalDateTime createdAt, Priority priority,
                          String category, String owner, List<SubTaskDto> subTasks) {
    }

    public record TaskRequest(String title, String description, LocalDate dueDate,
                              Priority priority, String category) {
    }

    private final TaskService taskService;

    public ApiController(TaskService taskService) {
        this.taskService = taskService;
    }

    /** Все задачи вместе с приоритетом, категорией, автором, временем создания и подзадачами. */
    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks().stream().map(ApiController::toDto).toList();
    }

    /** Имя и роли текущего пользователя - клиент по ним включает/выключает кнопки. */
    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority().replaceFirst("^ROLE_", ""))
                .toList();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("username", authentication.getName());
        result.put("roles", roles);
        return result;
    }

    /** Добавление задачи; автором становится пользователь, от имени которого пришёл запрос. */
    @PostMapping
    public ResponseEntity<Void> addTask(@RequestBody TaskRequest request, Authentication authentication) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Название задачи не может быть пустым");
        }
        Priority priority = request.priority() == null ? Priority.MEDIUM : request.priority();
        taskService.addTask(request.title().trim(), request.description(), request.dueDate(),
                priority, request.category(), authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /** Смена статуса. Только MODERATOR и ADMIN (как в веб-версии). */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public ResponseEntity<Void> updateTask(@PathVariable Long id, @RequestParam boolean completed) {
        taskService.updateTask(id, completed);
        return ResponseEntity.noContent().build();
    }

    /** Удаление (подзадачи и история удаляются каскадно). Только ADMIN (как в веб-версии). */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    private static TaskDto toDto(Task task) {
        List<SubTaskDto> subTasks = task.getSubTasks().stream()
                .map(ApiController::toDto)
                .toList();
        return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted(),
                task.getDueDate(), task.getCreatedAt(), task.getPriority(), task.getCategory(),
                task.getOwner() == null ? null : task.getOwner().getName(), subTasks);
    }

    private static SubTaskDto toDto(SubTask subTask) {
        return new SubTaskDto(subTask.getId(), subTask.getDescription(), subTask.isCompleted());
    }
}
