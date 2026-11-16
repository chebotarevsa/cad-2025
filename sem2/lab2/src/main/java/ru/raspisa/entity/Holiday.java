package ru.raspisa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HolidayType type;

    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(columnDefinition = "boolean default false")
    private boolean yearly;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public HolidayType getType() { return type; }
    public void setType(HolidayType type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isYearly() { return yearly; }
    public void setYearly(boolean yearly) { this.yearly = yearly; }
}
