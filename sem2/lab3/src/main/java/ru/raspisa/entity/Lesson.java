package ru.raspisa.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime time;

    private Integer orderNumber;

    @Column(columnDefinition = "boolean default false")
    private boolean special;

    private LocalDate date;

    private LocalTime endTime;

    @Column(columnDefinition = "boolean default false")
    private boolean customTime;

    public boolean isCustomTime() { return customTime; }
    public void setCustomTime(boolean customTime) { this.customTime = customTime; }

    private String title;

    @Lob
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lesson_photos", joinColumns = @JoinColumn(name = "lesson_id"))
    @Lob
    @Column(name = "photo")
    private List<String> photos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public Integer getOrderNumber() { return orderNumber; }
    public void setOrderNumber(Integer orderNumber) { this.orderNumber = orderNumber; }

    public boolean isSpecial() { return special; }
    public void setSpecial(boolean special) { this.special = special; }

    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Studio getStudio() { return studio; }
    public void setStudio(Studio studio) { this.studio = studio; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }
}
