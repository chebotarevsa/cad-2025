package ru.bsuedu.cad_2025;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {
    private List<Employee> employeeList = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong(1); // Генератор ID

    // Метод для добавления сотрудника в репозиторий (с генерацией ID)
    public void addEmployee(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(idGenerator.getAndIncrement());
        }
        employeeList.add(employee);
    }

    // Метод для получения всех сотрудников из репозитория
    public List<Employee> getAllEmployees() {
        return employeeList;
    }

    // Метод для удаления сотрудника из репозитория по его идентификатору
    public void deleteEmployee(Long id) {
        employeeList.removeIf(employee -> employee.getId().equals(id));
    }

    // Метод для поиска сотрудников по фамилии (без учета регистра, частичное совпадение)
    public List<Employee> getEmployeesByLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            return employeeList; // Если запрос пустой, вернуть всех
        }
        return employeeList.stream()
                .filter(employee -> employee.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Метод для обновления зарплаты сотрудника
    public void updateEmployeeSalary(Long id, double salary) {
        for (Employee employee : employeeList) {
            if (employee.getId().equals(id)) {
                employee.setSalary(salary);
                break;
            }
        }
    }

    // Метод для массового удаления всех сотрудников
    public void deleteAllEmployees() {
        employeeList.clear();
    }
}