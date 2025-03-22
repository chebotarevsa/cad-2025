package ru.bsuedu.cad.demo.app;

import org.springframework.stereotype.Component;

import ru.bsuedu.cad.demo.service.StudentService;

@Component
public class Client {
    final private  StudentService studentService;

    public Client(StudentService studentService) {
        this.studentService = studentService;
    }

    public void run() {
        studentService.createStudent("Ли Си Цин", 12002308);
    }
}
