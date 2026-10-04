package ru.bsuedu.cad.lab6;

public class Task {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private String priority;
    private String category;
    private String createdAt;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getPriority() {
        return priority;
    }

    public String getCategory() {
        return category;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
