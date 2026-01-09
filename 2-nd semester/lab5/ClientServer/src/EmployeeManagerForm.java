import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmployeeManagerForm extends JFrame {
    private JList<String> employeeList;
    private DefaultListModel<String> employeeListModel;
    private String authHeader;
    private JTextField searchField;
    private JButton searchButton;

    public EmployeeManagerForm(String authHeader) {
        this.authHeader = authHeader;
        setTitle("Employee Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель поиска
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchButton = new JButton("Поиск по фамилии");
        searchPanel.add(new JLabel("Фамилия:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Список сотрудников
        employeeListModel = new DefaultListModel<>();
        employeeList = new JList<>(employeeListModel);
        loadEmployees(); // Загрузка сотрудников из API
        add(new JScrollPane(employeeList), BorderLayout.CENTER);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Обновить");
        JButton deleteButton = new JButton("Удалить");
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчики событий
        refreshButton.addActionListener(e -> loadEmployees());
        
        searchButton.addActionListener(e -> {
                loadEmployees();
        });
        
        
        deleteButton.addActionListener(e -> {
            int selectedIndex = employeeList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selected = employeeListModel.getElementAt(selectedIndex);
                Long employeeId = extractIdFromString(selected);
                if (employeeId != null) {
                    deleteEmployee(employeeId);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Выберите сотрудника для удаления");
            }
        });
        
    }

    private void loadEmployees() {
        try {
            URL url = new URL("http://localhost:8080/api");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + authHeader);
            
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder jsonResponse = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    jsonResponse.append(inputLine);
                }
                in.close();
                
                parseEmployeesJson(jsonResponse.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки сотрудников");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка соединения: " + e.getMessage());
        }
    }


    private void parseEmployeesJson(String json) {
        json = json.trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        }
        
        employeeListModel.clear();
        
        if (json.isEmpty()) {
            employeeListModel.addElement("Нет сотрудников");
            return;
        }
        
        String[] employeesArray = json.split("\\},\\{");
        
        for (String employee : employeesArray) {
            employee = employee.replace("{", "").replace("}", "").trim();
            
            String id = extractField(employee, "id");
            String firstName = extractField(employee, "firstName");
            String lastName = extractField(employee, "lastName");
            String salary = extractField(employee, "salary");
            String position = extractField(employee, "position");
            
            if (id != null && firstName != null && lastName != null) {
                String displayText = String.format("ID: %s | %s %s | Должность: %s | Зарплата: %s",
                    id, firstName, lastName, 
                    position != null ? position : "Не указана",
                    salary != null ? salary : "0.0");
                employeeListModel.addElement(displayText);
            }
        }
    }

    private String extractField(String employee, String fieldName) {
        String[] fields = employee.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":");
            if (keyValue.length >= 2) {
                String key = keyValue[0].trim().replace("\"", "");
                // Объединяем все части значения после первого двоеточия
                StringBuilder valueBuilder = new StringBuilder();
                for (int i = 1; i < keyValue.length; i++) {
                    if (i > 1) valueBuilder.append(":");
                    valueBuilder.append(keyValue[i]);
                }
                String value = valueBuilder.toString().trim().replace("\"", "");
                
                if (key.equals(fieldName)) {
                    return value;
                }
            }
        }
        return null;
    }

    private Long extractIdFromString(String employeeString) {
        try {
            int idStart = employeeString.indexOf("ID: ");
            if (idStart != -1) {
                int idEnd = employeeString.indexOf(" |", idStart);
                if (idEnd != -1) {
                    String idStr = employeeString.substring(idStart + 4, idEnd).trim();
                    return Long.parseLong(idStr);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void deleteEmployee(Long employeeId) {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Вы уверены, что хотите удалить сотрудника?", 
            "Подтверждение удаления", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                URL url = new URL("http://localhost:8080/deleteEmployee/" + employeeId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Basic " + authHeader);
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                
                int responseCode = conn.getResponseCode();
                System.out.println("Delete Response Code: " + responseCode);
                
                if (responseCode >= 200 && responseCode < 300) {
                    JOptionPane.showMessageDialog(this, "Сотрудник успешно удален");
                    loadEmployees(); // Обновляем список
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при удалении сотрудника");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Ошибка соединения: " + e.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        // Пример использования
        String authHeader = "bW9kZXJhdG9yOnBhc3N3b3Jk"; // Замените на ваш заголовок авторизации
        SwingUtilities.invokeLater(() -> new EmployeeManagerForm(authHeader).setVisible(true));
    }
}