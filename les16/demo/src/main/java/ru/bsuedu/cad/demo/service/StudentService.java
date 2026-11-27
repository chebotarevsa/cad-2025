package ru.bsuedu.cad.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Student create(Long id, String name) {
        var student = new Student();
        student.setId(id);
        student.setName(name);

        studentRepository.save(student);
        return student;
    } 

    public List<Student> getAll() {
        return studentRepository.findAll();
    }
}
