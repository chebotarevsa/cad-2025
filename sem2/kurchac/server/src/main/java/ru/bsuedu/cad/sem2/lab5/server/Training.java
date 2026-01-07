package ru.bsuedu.cad.sem2.lab5.server;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;  // Название тренировки
    private String description;  // Описание тренировки
    private LocalDateTime schedule;  // Время проведения тренировки

    @ManyToOne
    @JoinColumn(name = "trainer_id")  // Связь с тренером
    private Trainer trainer;

    @ManyToMany(mappedBy = "trainings")
    private List<Client> clients;  // Список клиентов, записанных на тренировку

    private boolean completed = false; // Статус тренировки (завершена или нет)

    // Constructors
    public Training() {}

    public Training(String title, String description, LocalDateTime schedule, Trainer trainer) {
        this.title = title;
        this.description = description;
        this.schedule = schedule;
        this.trainer = trainer;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getSchedule() {
        return schedule;
    }

    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Training [" + 
               "id=" + id + 
               ", title=" + title + 
               ", description=" + description + 
               ", schedule=" + schedule + 
               ", trainer=" + trainer + 
               ", completed=" + completed + "]";
    }
}
