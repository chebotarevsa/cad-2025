package ru.bsuedu.cad.demo.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import jakarta.transaction.Transactional;
import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.repository.GroupRepository;
import ru.bsuedu.cad.demo.repository.StudentRepository;

@Service

public class StudentService {
    final private StudentRepository studentRepository;
    final private GroupRepository groupRepository;

    public StudentService(@Qualifier("jpaStudentRepository") StudentRepository studentRepository, @Qualifier("jpaGroupRepository") GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    
    @Transactional
    public void createStudent(String name, int groupNumber) {
        var group = groupRepository.findByNumber(groupNumber).get(0);
        var student = new Student();
        student.setName(name);
        student.setGroup(group);
        studentRepository.save(student);
    }

}
