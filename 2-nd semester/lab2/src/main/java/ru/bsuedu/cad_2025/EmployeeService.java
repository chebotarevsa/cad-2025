package ru.bsuedu.cad_2025;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Метод для добавления нового сотрудника
    public void addEmployee(Employee employee) {
        employeeRepository.addEmployee(employee);
    }

    // Метод для получения всех сотрудников
    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    // Метод для удаления сотрудника по его идентификатору
    public void deleteEmployee(Long id) {
        employeeRepository.deleteEmployee(id);
    }

    public void updateEmployeeSalary(Long id, double salary) {
        employeeRepository.updateEmployeeSalary(id, salary);
    }

    public List<Employee> getEmployeesByLastName(String lastName) {
        return employeeRepository.getEmployeesByLastName(lastName);
    }
}
