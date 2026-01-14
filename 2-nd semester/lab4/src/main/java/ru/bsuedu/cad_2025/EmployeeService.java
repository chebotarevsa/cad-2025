package ru.bsuedu.cad_2025;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeHistoryRepository employeeHistoryRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeHistoryRepository employeeHistoryRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeHistoryRepository = employeeHistoryRepository;
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "anonymous";
    }

    // Метод для добавления нового сотрудника
    public void addEmployee(Employee employee) {
        Employee saved = employeeRepository.save(employee);
        employeeHistoryRepository.save(new EmployeeHistory(saved.getId(), "ADD", null, null, "Employee added", getCurrentUsername()));
    }

    // Метод для получения всех сотрудников
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Метод для удаления сотрудника по его идентификатору
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.deleteById(id);
        employeeHistoryRepository.save(new EmployeeHistory(id, "DELETE", null, "Employee existed", null, getCurrentUsername()));
    }

    // Метод для обновления зарплаты сотрудника
    public void updateEmployeeSalary(Long id, double salary) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        double oldSalary = employee.getSalary();
        employee.setSalary(salary);
        employeeRepository.save(employee);
        employeeHistoryRepository.save(new EmployeeHistory(id, "UPDATE", "salary", String.valueOf(oldSalary), String.valueOf(salary), getCurrentUsername()));
    }

    // Метод для поиска сотрудников по фамилии
    public List<Employee> getEmployeesByLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            return employeeRepository.findAll();
        }
        return employeeRepository.findByLastNameContainingIgnoreCase(lastName.trim());
    }

    // Метод для массового удаления всех сотрудников
    public void deleteAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee emp : employees) {
            employeeHistoryRepository.save(new EmployeeHistory(emp.getId(), "DELETE", null, "Employee existed", null, getCurrentUsername()));
        }
        employeeRepository.deleteAll();
    }

    // Метод для получения истории изменений сотрудника
    public List<EmployeeHistory> getEmployeeHistory(Long employeeId) {
        return employeeHistoryRepository.findByEmployeeIdOrderByTimestampDesc(employeeId);
    }
}
