package ru.raspisa.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "task_history")
public class TaskHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public Instant changedAt = Instant.now();
    @Column(nullable = false, length = 40)
    public String action;
    @Column(columnDefinition = "text")
    public String oldState;
    @Column(nullable = false, columnDefinition = "text")
    public String newState;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    public Task task;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public User actor;
}
