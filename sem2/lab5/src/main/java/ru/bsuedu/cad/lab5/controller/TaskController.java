package ru.bsuedu.cad.lab5.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import ru.bsuedu.cad.lab5.model.Task;
import ru.bsuedu.cad.lab5.service.TaskService;

@Controller
public class TaskController {

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("tasks", taskService.getAllTasks());
		return "index";
	}

	@PostMapping("/addTask")
	public String addTask(@ModelAttribute Task task) {
		taskService.addTask(task);
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

	@GetMapping("/api")
	@PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
	public ResponseEntity<List<Task>> getTasksApi() {
		return ResponseEntity.ok(taskService.getAllTasks());
	}

	@PostMapping("/api")
	@PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
	public ResponseEntity<Task> addTaskApi(@RequestBody Task task) {
		return ResponseEntity.ok(taskService.addTask(task));
	}

	@DeleteMapping("/api/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteTaskApi(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}
}
