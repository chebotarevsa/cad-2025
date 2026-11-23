package ru.raspisa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sub_task")
public class SubTask {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, length = 500)
    public String description;
    @Column(nullable = false)
    public boolean completed;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    public Task task;
}
