package ru.bsuedu.cad.demo.view.student;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.view.main.MainView;


@Route(value = "students", layout = MainView.class)
@ViewController(id = "Student.list")
@ViewDescriptor(path = "student-list-view.xml")
@LookupComponent("studentsDataGrid")
@DialogMode(width = "64em")
public class StudentListView extends StandardListView<Student> {
}