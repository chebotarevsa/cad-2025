package ru.bsuedu.cad.sem2.lab5.server;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user; // Связь с пользователем (User), определяющим роль клиента

    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime dateOfBirth;

    @ManyToMany
    @JoinTable(
      name = "client_trainings", 
      joinColumns = @JoinColumn(name = "client_id"), 
      inverseJoinColumns = @JoinColumn(name = "training_id"))
    private List<Training> trainings; // Список тренировок, на которые записан клиент

    @ManyToOne
    private Subscription subscription; // Абонемент клиента (заглушка)

    // Constructors
    public Client() {}

    public Client(User user, String firstName, String lastName, String email, LocalDateTime dateOfBirth) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "Client [" + 
               "id=" + id + 
               ", user=" + user + 
               ", firstName=" + firstName + 
               ", lastName=" + lastName + 
               ", email=" + email + 
               ", dateOfBirth=" + dateOfBirth + 
               ", subscription=" + subscription + "]";
    }
}
