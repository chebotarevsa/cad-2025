package ru.raspisa.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {
    public enum Status { TODO, IN_PROGRESS, DONE }
    public enum Priority { LOW, NORMAL, HIGH }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false, length = 200)
    public String title;
    @Column(nullable = false, columnDefinition = "text")
    public String description = "";
    @Column(nullable = false, updatable = false)
    public Instant createdAt = Instant.now();
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    public Priority priority = Priority.NORMAL;
    @Column(nullable = false, length = 100)
    public String category;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    public Status status = Status.TODO;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public User owner;
    @Column(nullable = false)
    public boolean deleted;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    public List<SubTask> subtasks = new ArrayList<>();
}
