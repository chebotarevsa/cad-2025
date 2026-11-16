package ru.raspisa.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Studio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;

    @Lob
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "studio_photos", joinColumns = @JoinColumn(name = "studio_id"))
    @Lob
    @Column(name = "photo")
    private List<String> photos = new ArrayList<>();

    @Lob
    private String teacherPhoto;

    @OneToOne(mappedBy = "studio")
    private Teacher teacher;

    @OneToMany(mappedBy = "studio")
    private List<Lesson> lessons = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }

    public String getTeacherPhoto() { return teacherPhoto; }
    public void setTeacherPhoto(String teacherPhoto) { this.teacherPhoto = teacherPhoto; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }
}
