package ru.bsuedu.cad.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ru.bsuedu.cad.demo.config.TestConfigDB;
import ru.bsuedu.cad.demo.entity.Group;
import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.repository.GroupRepository;
import ru.bsuedu.cad.demo.repository.StudentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfigDB.class)
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class StudentServiceIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setup() {
        Group group = new Group();
        group.setNumber(101);
        group.setDescription("101");
        groupRepository.save(group);
    }

    @Test
    void testCreateStudentAndFindById() {
        studentService.createStudent("Alex", 101);

        List<Student> students = studentRepository.findAll();
        assertEquals(1, students.size());

        Student student = students.get(0);
        assertEquals("Alex", student.getName());
        assertEquals(101, student.getGroup().getNumber());

        Student found = studentService.findById(student.getId());
        assertEquals(student.getId(), found.getId());
    }

    @Test
    void testFindAllStudents() {
        studentService.createStudent("Anna", 101);
        studentService.createStudent("Dmitry", 101);

        List<Student> students = studentService.findAllStudents();
        assertEquals(2, students.size());
    }
}