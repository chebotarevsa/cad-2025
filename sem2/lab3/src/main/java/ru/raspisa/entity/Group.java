package ru.raspisa.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "number")
    private Integer legacyNumber;

    @Column(name = "code", length = 32)
    private String code;

    @Enumerated(EnumType.STRING)
    private Shift shift = Shift.MORNING;

    @ManyToOne
    @JoinColumn(name = "curator_id")
    private Teacher curator;

    @OneToMany(mappedBy = "group")
    private List<Lesson> lessons = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumber() {
        if (code != null && !code.isBlank()) return code;
        return legacyNumber == null ? null : String.valueOf(legacyNumber);
    }

    public void setNumber(String number) { this.code = number; }

    public Integer getLegacyNumber() { return legacyNumber; }
    public void setLegacyNumber(Integer legacyNumber) { this.legacyNumber = legacyNumber; }

    public Shift getShift() { return shift; }
    public void setShift(Shift shift) { this.shift = shift; }

    public Teacher getCurator() { return curator; }
    public void setCurator(Teacher curator) { this.curator = curator; }

    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }
}
