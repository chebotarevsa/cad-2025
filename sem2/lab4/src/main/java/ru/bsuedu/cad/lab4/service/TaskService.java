package ru.bsuedu.cad.lab4.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.bsuedu.cad.lab4.model.Task;
import ru.bsuedu.cad.lab4.repository.TaskRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public void addTask(Task task) {
		taskRepository.save(task);
	}

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public void deleteTask(Long id) {
		taskRepository.deleteById(id);
	}

	public void updateTask(Long id, boolean completed) {
		Task task = taskRepository.findById(id).orElseThrow();
		task.setCompleted(completed);
		taskRepository.save(task);
	}
}
