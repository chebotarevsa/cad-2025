package ru.bsuedu.cad.sem2.lab5.server;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;  // Тип абонемента (например, "1 месяц", "3 месяца")
    private int durationInDays;  // Продолжительность абонемента в днях

    // Constructors
    public Subscription() {}

    public Subscription(String type, int durationInDays) {
        this.type = type;
        this.durationInDays = durationInDays;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(int durationInDays) {
        this.durationInDays = durationInDays;
    }

    @Override
    public String toString() {
        return "Subscription [" +
               "id=" + id +
               ", type='" + type + '\'' +
               ", durationInDays=" + durationInDays + "]";
    }
}
