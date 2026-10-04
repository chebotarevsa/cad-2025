package ru.bsuedu.cad.lab3.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import ru.bsuedu.cad.lab3.model.Task;

@Repository
public class TaskRepository {

	private final List<Task> taskList = new ArrayList<>();

	public void addTask(Task task) {
		taskList.add(task);
	}

	public List<Task> getAllTasks() {
		return taskList;
	}

	public void deleteTask(Long id) {
		taskList.removeIf(task -> task.getId().equals(id));
	}
}
