package ru.bsuedu.cad.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ch.qos.logback.core.net.server.Client;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bsuedu.cad.demo.ConfigJpa;
import ru.bsuedu.cad.demo.entity.Student;
import ru.bsuedu.cad.demo.repository.GroupRepository;
import ru.bsuedu.cad.demo.service.GroupService;
import ru.bsuedu.cad.demo.service.StudentService;

@Component
public class StudentServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private StudentService studentService;
    private GroupService groupService;

    @Override
    public void init() throws ServletException {
        // Инициализация Spring-контекста вручную
        // ApplicationContext context = new AnnotationConfigApplicationContext(ConfigJpa.class);
        // this.studentService = context.getBean(StudentService.class);
        // this.groupService = context.getBean(GroupService.class);
        ServletContext servletContext = getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.studentService = context.getBean(StudentService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        for (Student s : studentService.findAllStudents()) {
            out.println("<h1>" + s.getName() + "</h1>");
        }
        resp.flushBuffer();
    }

}
