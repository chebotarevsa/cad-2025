package ru.bsuedu.cad.demo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ru.bsuedu.cad.demo.App;
import ru.bsuedu.cad.demo.repository.GroupRepository;
import ru.bsuedu.cad.demo.service.GroupService;
import ru.bsuedu.cad.demo.service.StudentService;

@Component
public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    final private  StudentService studentService;
    final private  GroupService groupService;

    public Client(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService; 
    }

    public void run() {
        studentService.createStudent("Ли Си Цин", 12002308);
       // for ( var g: groupService.searchGroupByPartName("1112")) {
       //     LOGGER.info(g.toString());
       // }

       studentService.findStudentByNamePageable("С", 1, 2);
    }
}
