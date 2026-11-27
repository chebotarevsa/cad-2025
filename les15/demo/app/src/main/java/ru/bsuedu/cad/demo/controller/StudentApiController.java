package ru.bsuedu.cad.demo.controller;

import java.util.List;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.service.GroupService;
import ru.bsuedu.cad.demo.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentApiController {
    
    final private StudentService studentService;
    final private GroupService groupService;
    
    
    public StudentApiController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }


    @GetMapping("")
    public List<Student>  getStudents(Model model) {   
        List<Student> students = studentService.findAllStudents();
        return students;
    }
}
