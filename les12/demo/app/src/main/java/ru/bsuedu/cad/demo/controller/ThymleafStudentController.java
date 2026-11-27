package ru.bsuedu.cad.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.model.StudentModel;
import ru.bsuedu.cad.demo.service.GroupService;
import ru.bsuedu.cad.demo.service.StudentService;

@Controller
@RequestMapping("/thymleaf/students")
public class ThymleafStudentController {
    
    final private StudentService studentService;
    final private GroupService groupService;
    
    
    public ThymleafStudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }


    @GetMapping("")
    public String getStudents(Model model) {
        List<Student> students = studentService.findAllStudents();
        model.addAttribute("students", students);
        return "student";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("groups", groupService.getAllGroup());
        return "create-student";
    }

    @PostMapping("")
    public String submitForm(@ModelAttribute StudentModel student) {
        studentService.createStudent(student.getName(), student.getGroup());
        System.out.println("Сохранён студент: " + student.getName() + " (" + student.getGroup() + ")");
        return "redirect:/thymleaf/students"; // переадресация на список
    }
}
