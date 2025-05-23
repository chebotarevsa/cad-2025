package ru.bsuedu.cad.demo.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "demo_course")
public class Course extends AbstractEntity {

    @Column(name = "DESCRIPTION", unique = false, nullable = false, length = 100)
    private String description;


    @ManyToMany
    @JoinTable(
         name = "demo_group_course",
         joinColumns = @JoinColumn(name = "COURSE_ID"),
         inverseJoinColumns = @JoinColumn(name = "GROUP_ID")
     )
    private List<Group> groups;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Group> getGroup() {
        return groups;
    }

    public void setGroup(List<Group> groups) {
        this.groups = groups;
    }
}
