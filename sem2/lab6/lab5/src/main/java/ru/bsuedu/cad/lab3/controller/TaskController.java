package ru.bsuedu.cad.lab3.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.bsuedu.cad.lab3.model.Priority;
import ru.bsuedu.cad.lab3.service.TaskService;

@Controller
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("priorities", Priority.values());
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam String title,
                          @RequestParam(required = false) String description,
                          @RequestParam(required = false)
                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
                          @RequestParam(defaultValue = "MEDIUM") Priority priority,
                          @RequestParam(required = false) String category,
                          Principal principal) {
        taskService.addTask(title, description, dueDate, priority, category, principal.getName());
        return "redirect:/";
    }

    @GetMapping("/deleteTask/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }

    @PostMapping("/updateTask/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String updateTask(@PathVariable Long id, @RequestParam boolean completed) {
        taskService.updateTask(id, completed);
        return "redirect:/";
    }

    @PostMapping("/addSubTask/{taskId}")
    public String addSubTask(@PathVariable Long taskId, @RequestParam String description) {
        taskService.addSubTask(taskId, description);
        return "redirect:/";
    }

    @PostMapping("/updateSubTask/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public String updateSubTask(@PathVariable Long id, @RequestParam boolean completed) {
        taskService.updateSubTask(id, completed);
        return "redirect:/";
    }
}
