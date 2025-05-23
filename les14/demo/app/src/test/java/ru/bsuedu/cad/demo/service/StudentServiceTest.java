package ru.bsuedu.cad.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.entity.Group;
import ru.bsuedu.cad.demo.repository.GroupRepository;
import ru.bsuedu.cad.demo.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private StudentService studentService;

    private Group group;
    private Student student;

    @BeforeEach
    void setUp() {
        group = new Group();
        group.setId(1L);
        group.setNumber(101);

        student = new Student();
        student.setId(1L);
        student.setName("Иван Иванов");
        student.setGroup(group);
    }

    @Test
    void createStudent_ShouldSaveStudent() {
        when(groupRepository.searchByNumber(101)).thenReturn(List.of(group));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        studentService.createStudent("Иван Иванов", 101);

        verify(groupRepository).searchByNumber(101);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void findById_WhenStudentExists_ShouldReturnStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student found = studentService.findById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getName()).isEqualTo("Иван Иванов");

        verify(studentRepository).findById(1L);
    }

    @Test
    void findById_WhenStudentDoesNotExist_ShouldThrowException() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.findById(2L))
                .isInstanceOf(java.util.NoSuchElementException.class);

        verify(studentRepository).findById(2L);
    }

    @Test
    void findStudentByNamePageable_ShouldLogInformation() {
        Page<Student> studentPage = new PageImpl<>(List.of(student));

        when(studentRepository.findByNameContaining(eq("Иван"), any(Pageable.class)))
                .thenReturn(studentPage);

        studentService.findStudentByNamePageable("Иван", 0, 10);

        verify(studentRepository).findByNameContaining(eq("Иван"), any(Pageable.class));
    }

    @Test
    void findAllStudents_ShouldReturnListOfStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> students = studentService.findAllStudents();

        assertThat(students).hasSize(1);
        assertThat(students.get(0).getName()).isEqualTo("Иван Иванов");

        verify(studentRepository).findAll();
    }
}