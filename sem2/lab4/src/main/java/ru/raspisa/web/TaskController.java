package ru.raspisa.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.raspisa.service.TaskService;
import ru.raspisa.service.TaskService.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@PreAuthorize("hasAnyRole('ADMIN', 'METHODIST')")
public class TaskController {
    private final TaskService tasks;
    public TaskController(TaskService tasks) { this.tasks = tasks; }

    @GetMapping
    public List<TaskView> list() { return tasks.list(); }
    @PostMapping
    public ResponseEntity<TaskView> create(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tasks.create(request));
    }
    @PutMapping("/{id}")
    public TaskView update(@PathVariable long id, @Valid @RequestBody TaskRequest request) {
        return tasks.update(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) { tasks.delete(id); }
    @GetMapping("/{id}/history")
    public List<HistoryView> history(@PathVariable long id) { return tasks.history(id); }
    @PostMapping("/{id}/subtasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskView addSubtask(@PathVariable long id, @Valid @RequestBody SubtaskRequest request) {
        return tasks.addSubtask(id, request);
    }
    @PutMapping("/{id}/subtasks/{subtaskId}")
    public TaskView updateSubtask(@PathVariable long id, @PathVariable long subtaskId,
            @Valid @RequestBody SubtaskRequest request) { return tasks.updateSubtask(id, subtaskId, request); }
    @DeleteMapping("/{id}/subtasks/{subtaskId}")
    public TaskView deleteSubtask(@PathVariable long id, @PathVariable long subtaskId) {
        return tasks.deleteSubtask(id, subtaskId);
    }
}
