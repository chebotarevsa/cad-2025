package ru.bsuedu.cad.demo.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "demo_course")
public class Course extends AbstractEntity{

    @Column(name="DESCRIPTION", unique = false, nullable = false, length = 100)
    private String description;


    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();
}
