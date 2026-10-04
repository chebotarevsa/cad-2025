package ru.bsuedu.cad.lab2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.bsuedu.cad.lab2.model.Task;
import ru.bsuedu.cad.lab2.service.TaskService;

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
	public String deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return "redirect:/";
	}

	@PostMapping("/updateTask/{id}")
	public String updateTask(@PathVariable Long id, @RequestParam boolean completed) {
		taskService.updateTask(id, completed);
		return "redirect:/";
	}
}
