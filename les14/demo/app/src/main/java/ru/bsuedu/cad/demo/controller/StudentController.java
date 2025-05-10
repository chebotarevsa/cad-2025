package ru.bsuedu.cad.demo.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.model.StudentModel;
import ru.bsuedu.cad.demo.service.GroupService;
import ru.bsuedu.cad.demo.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {
    
    final private StudentService studentService;
    final private GroupService groupService;
    
    
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }


    @GetMapping("")
    public String getStudents(Model model) {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();      
        List<Student> students = studentService.findAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("name", currentPrincipalName);
        return "student";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.getAllGroup());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName(); 
        model.addAttribute("name", currentPrincipalName);
        return "create-student";
    }

    @PostMapping("")
    public String submitForm(@ModelAttribute StudentModel student) {
        studentService.createStudent(student.getName(), student.getGroup());
        System.out.println("Сохранён студент: " + student.getName() + " (" + student.getGroup() + ")");
        return "redirect:/students"; // переадресация на список
    }
}
