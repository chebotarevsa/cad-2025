package ru.bsuedu.cad.sem2.lab5.client;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TaskManagerForm extends JFrame {
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private String authHeader;

    public TaskManagerForm(String authHeader) {
        this.authHeader = authHeader;
        setTitle("Task Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Список задач
        // taskListModel = new DefaultListModel<>();
        // taskList = new JList<>(taskListModel);
        // loadTasks(); // Загрузка задач из API
        // add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Модель таблицы с колонками
        String[] columns = { "id", "№", "Название", "Описание", "Срок выполнения", "Статус", "Автор" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Все ячейки — только для чтения
            }
        };
        taskTable = new JTable(tableModel);
        taskTable.getColumnModel().removeColumn(taskTable.getColumnModel().getColumn(0));
        add(new JScrollPane(taskTable), BorderLayout.CENTER);

        // Панель для кнопок
        JPanel buttonPanel = new JPanel();

        JButton refreshButton = new JButton("Обновить");
        JButton addButton = new JButton("Добавить задачу");
        JButton deleteButton = new JButton("Удалить задачу");

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчики для кнопок

        // Обновить список задач
        refreshButton.addActionListener(e -> loadTasks());
        // Добавить задачу
        addButton.addActionListener(e -> addNewTask());

        // Удалить задачу
        deleteButton.addActionListener(e -> deleteSelectedTask());
    }

    @SuppressWarnings("deprecation")
    private void loadTasks() {
        try {
            URL url = new URL("http://localhost:8080/api");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + authHeader);

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    jsonResponse.append(inputLine);
                }
                in.close();

                // Парсинг JSON вручную
                parseJson(jsonResponse.toString());
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки задач");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedTask() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите задачу для удаления.");
            return;
        }

        // Получаем id задачи (скрытая первая колонка)
        String taskIdStr = (String) tableModel.getValueAt(selectedRow, 0);
        Long taskId;
        try {
            taskId = Long.parseLong(taskIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Некорректный ID задачи.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить задачу №" + (selectedRow + 1) + "?", "Подтверждение удаления",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Отправляем DELETE-запрос на сервер
        try {
            URL url = new URL("http://localhost:8080/api/" + taskId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", "Basic " + authHeader);

            System.out.println("Deleting task with ID: " + taskId);
            System.out.println("Auth Header (full): Basic " + authHeader);

            System.out.println("Response Code: " + conn.getResponseCode());
            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || responseCode == 204) {
                // Успешно удалено
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Задача успешно удалена.");
                renumberTasks(); // Перенумеруем столбец "№"
            } else if (responseCode == 404) {
                JOptionPane.showMessageDialog(this, "Задача не найдена.");
            } else if (responseCode == 403) {
                JOptionPane.showMessageDialog(this, "Нет прав на удаление задачи.");
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка сервера: " + responseCode);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка соединения с сервером.");
            e.printStackTrace();
        }
    }

    private void renumberTasks() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(i + 1, i, 1); // Обновляем столбец "№"
        }
    }

    private void addNewTask() {
        // Создаём форму для ввода данных
        JTextField titleField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField deadlineField = new JTextField("dd.MM.yyyy HH:mm", 20); // Подсказка

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(new JLabel("Название:"));
        formPanel.add(titleField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Описание:"));
        formPanel.add(descriptionField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Срок (dd.MM.yyyy HH:mm):"));
        formPanel.add(deadlineField);

        // Показываем диалог
        int result = JOptionPane.showConfirmDialog(this, formPanel, "Добавить задачу", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return; // Отмена
        }

        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String deadlineStr = deadlineField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Название задачи обязательно.");
            return;
        }

        // Формируем JSON
        StringBuilder jsonInput = new StringBuilder();
        jsonInput.append("{");
        jsonInput.append("\"title\":\"").append(escapeJson(title)).append("\"");
        if (!description.isEmpty()) {
            jsonInput.append(",\"description\":\"").append(escapeJson(description)).append("\"");
        }
        if (!deadlineStr.isEmpty() && !deadlineStr.equals("dd.MM.yyyy HH:mm")) {
            try {
                LocalDateTime deadline = LocalDateTime.parse(deadlineStr,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                jsonInput.append(",\"deadLine\":\"").append(deadline).append("\"");
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Неверный формат даты. Используйте: dd.MM.yyyy HH:mm");
                return;
            }
        }
        jsonInput.append("}");

        // Отправляем POST-запрос на /api
        try {
            URL url = new URL("http://localhost:8080/api");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + authHeader);
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Отправляем тело запроса
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 201) {
                // Успешно добавлено, обновляем таблицу
                JOptionPane.showMessageDialog(this, "Задача успешно добавлена.");
                loadTasks(); // Перезагружаем список
            } else if (responseCode == 400) {
                JOptionPane.showMessageDialog(this, "Некорректные данные.");
            } else if (responseCode == 403) {
                JOptionPane.showMessageDialog(this, "Нет прав на добавление задач.");
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка сервера: " + responseCode);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка соединения с сервером.");
            e.printStackTrace();
        }
    }

    private String escapeJson(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void parseJson(String json) {
        // Удаляем квадратные скобки
        json = json.trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        }

        // Разделяем задачи по "},{" и обрабатываем каждую задачу
        String[] tasksArray = json.split("\\},\\{");
        int taskNumber = 1; // Счетчик задач

        // Очищаем таблицу перед добавлением новых задач
        tableModel.setRowCount(0);

        for (String task : tasksArray) {
            // Удаляем фигурные скобки
            task = task.replace("{", "").replace("}", "").trim();

            String id = extractField(task, "id");
            String title = extractField(task, "title");
            String description = extractField(task, "description");
            String deadline = extractField(task, "deadLine");
            String status = extractField(task, "completed");
            String author = extractField(task, "name");

            status = formateStatus(status);
            deadline = formateDate(deadline);

            tableModel.addRow(new Object[] { id, taskNumber++, title, description, deadline, status, author });
        }
    }

    private String extractField(String task, String fieldName) {
        String[] fields = task.split(",");
        for (String field : fields) {
            String[] keyValue = field.split(":", 2);

            String key = keyValue[0].trim().replace("\"", ""); // Убираем кавычки
            if (key.equals(fieldName)) {
                String value = keyValue[1].trim().replace("\"", ""); // Убираем кавычки
                return value; // Возвращаем значение поля
            }
        }
        return null; // Если поле не найдено
    }

    private String formateStatus(String status) {
        if (status.equals("true")) {
            return "Завершена";
        } else {
            return "В работе";
        }
    }

    private String formateDate(String date) {
        if (date.equals("null") || date.isEmpty()) {
            return "";
        }
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }
}