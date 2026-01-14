package ru.bsuedu.cad_2025;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "index";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeService.addEmployee(employee);
        return "redirect:/";
    }

    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/";
    }

    @PostMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable Long id, @RequestParam double salary) {
        employeeService.updateEmployeeSalary(id, salary);
        return "redirect:/";
    }

    // Endpoint для поиска по фамилии
    @GetMapping("/search")
    public String searchEmployees(@RequestParam(required = false, defaultValue = "") String lastName, Model model) {
        List<Employee> searchResults = employeeService.getEmployeesByLastName(lastName);
        model.addAttribute("employees", searchResults);
        model.addAttribute("searchQuery", lastName);
        return "index";
    }
}