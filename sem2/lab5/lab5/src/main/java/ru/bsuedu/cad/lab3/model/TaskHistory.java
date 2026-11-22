package ru.bsuedu.cad.lab3.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_history")
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime changedAt;

    /** Состояние до изменения; у записи о создании задачи пусто. */
    private String oldState;

    @Column(nullable = false)
    private String newState;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    protected TaskHistory() {
    }

    public TaskHistory(String oldState, String newState) {
        this.oldState = oldState;
        this.newState = newState;
        this.changedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getOldState() { return oldState; }
    public String getNewState() { return newState; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }
}
