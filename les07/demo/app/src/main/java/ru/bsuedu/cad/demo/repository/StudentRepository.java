package ru.bsuedu.cad.demo.repository;

import java.util.List;

import ru.bsuedu.cad.demo.entity.Student;

public interface StudentRepository {
    void save(Student student);
    Student findById(Long id);
    void update(Student student);
    void delete(Student student);
    List<Student> findByName(String name);
}
