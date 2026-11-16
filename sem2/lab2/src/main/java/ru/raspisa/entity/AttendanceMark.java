package ru.raspisa.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"studentId", "lessonId", "lessonDate"}))
public class AttendanceMark {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false) public Long groupId;
    @Column(nullable = false) public Long studentId;
    @Column(nullable = false) public Long lessonId;
    @Column(nullable = false) public LocalDate lessonDate;
    @Column(nullable = false, length = 1) public String mark;
    public String subject;
    public LocalTime lessonTime;
}
