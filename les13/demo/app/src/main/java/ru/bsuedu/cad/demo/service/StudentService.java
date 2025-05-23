package ru.bsuedu.cad.demo.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.repository.GroupRepository;
import ru.bsuedu.cad.demo.repository.StudentRepository;

@Service
public class StudentService {

     private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);
    final private StudentRepository studentRepository;
    final private GroupRepository groupRepository;

    public StudentService(StudentRepository studentRepository,  GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    
    @Transactional
    public void createStudent(String name, int groupNumber) {
        var group = groupRepository.searchByNumber(groupNumber).get(0);
        var student = new Student();
        student.setName(name);
        student.setGroup(group);
        studentRepository.save(student);
    }


    public Student findById(Long id) {
        return  studentRepository.findById(id).orElseThrow();
    }


    public void findStudentByNamePageable(String name, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name").ascending());

        var studentPage = studentRepository.findByNameContaining(name, pageable);

        LOGGER.info("Общее число записей: " + studentPage.getTotalElements());
        LOGGER.info("Всего страниц: " + studentPage.getTotalPages());
    
        List<Student> students = studentPage.getContent();
        students.forEach(student -> System.out.println(student.getName()));
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

}
