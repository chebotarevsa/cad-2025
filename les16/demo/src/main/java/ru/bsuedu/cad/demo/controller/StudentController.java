package ru.bsuedu.cad.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.service.StudentService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/student")
public class StudentController {
    
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping("")
    public Student create(@RequestBody Student entity) {
        return  studentService.create(entity.getId(), entity.getName());      
    }

    @GetMapping("")
    public List<Student> get() {
        return  studentService.getAll();
    }

}
