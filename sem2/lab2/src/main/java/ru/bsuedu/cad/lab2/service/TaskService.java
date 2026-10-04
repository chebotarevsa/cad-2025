package ru.bsuedu.cad.lab2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ru.bsuedu.cad.lab2.model.Task;
import ru.bsuedu.cad.lab2.repository.TaskRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public void addTask(Task task) {
		taskRepository.addTask(task);
	}

	public List<Task> getAllTasks() {
		return taskRepository.getAllTasks();
	}

	public void deleteTask(Long id) {
		taskRepository.deleteTask(id);
	}

	public void updateTask(Long id, boolean completed) {
		for (Task task : taskRepository.getAllTasks()) {
			if (task.getId().equals(id)) {
				task.setCompleted(completed);
				break;
			}
		}
	}
}
