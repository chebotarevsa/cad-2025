package ru.bsuedu.cad.lab2.model;

public class Task {

	private static Long nextId = 1L;

	private final Long id;
	private String title;
	private String description;
	private boolean completed;

	public Task() {
		this.id = nextId++;
	}

	public Task(String title, String description) {
		this.id = nextId++;
		this.title = title;
		this.description = description;
		this.completed = false;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
