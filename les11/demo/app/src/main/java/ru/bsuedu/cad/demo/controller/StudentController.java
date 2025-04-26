package ru.bsuedu.cad.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.service.StudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    
    final private StudentService studentService;
    
    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/{id}")
    public Student getUser(@PathVariable(name = "id") Long id) {
        return studentService.findById(id);
    }
}
