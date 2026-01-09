package ru.bsuedu.cad_2025;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Веб-методы

    @GetMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String index(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "index";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute Employee employee) {
        employeeService.addEmployee(employee);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable Long id, @RequestParam double salary) {
        employeeService.updateEmployeeSalary(id, salary);
        return "redirect:/";
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String searchEmployees(@RequestParam(required = false, defaultValue = "") String lastName, Model model) {
        List<Employee> searchResults = employeeService.getEmployeesByLastName(lastName);
        model.addAttribute("employees", searchResults);
        model.addAttribute("searchQuery", lastName);
        return "index";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deleteAllEmployees")
    public String deleteAllEmployees() {
        employeeService.deleteAllEmployees();
        return "redirect:/";
    }

    @GetMapping("/employee/{id}/history")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String getEmployeeHistory(@PathVariable Long id, Model model) {
        List<EmployeeHistory> history = employeeService.getEmployeeHistory(id);
        model.addAttribute("history", history);
        return "history";
    }

    // REST-метод

    @GetMapping("/api")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
}