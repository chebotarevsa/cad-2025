package ru.bsuedu.cad.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.service.StudentService;

@Controller
@RequestMapping("/jsp/students")
public class JSPStudentController {
    
    final private StudentService studentService;
    
    
    public JSPStudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/{id}")
    public Student getStudent(@PathVariable(name = "id") Long id) {
        return studentService.findById(id);
    }

    @GetMapping("")
    public String getStudents(Model model) {
        List<Student> students = studentService.findAllStudents();
        model.addAttribute("students", students);
        return "student"; // Возвращаем имя JSP-страницы 
    }
}
