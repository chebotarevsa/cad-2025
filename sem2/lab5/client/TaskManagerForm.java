import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskManagerForm extends JFrame {
    static class HttpResult {
        final int status;
        final String body;

        HttpResult(int status, String body) {
            this.status = status;
            this.body = body;
        }
    }

    private static class TaskItem {
        final long id;
        final String title;
        final String description;
        final String category;
        final String priority;
        final String status;
        final String owner;
        final String createdAt;

        TaskItem(long id, String title, String description, String category,
                 String priority, String status, String owner, String createdAt) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.category = category;
            this.priority = priority;
            this.status = status;
            this.owner = owner;
            this.createdAt = createdAt;
        }
    }

    private final String baseUrl;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JComboBox<String> sortBox;
    private final List<TaskItem> items = new ArrayList<>();

    public TaskManagerForm(String baseUrl, String username, String role) {
        this.baseUrl = baseUrl;

        setTitle("Подготовка мероприятий – " + username + " (" + role + ")");
        setSize(920, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        topPanel.add(new JLabel("Сортировка:"));
        sortBox = new JComboBox<>(new String[]{"По дате создания", "По приоритету", "По статусу", "По названию"});
        sortBox.addActionListener(e -> renderTable());
        topPanel.add(sortBox);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Название", "Описание", "Категория", "Приоритет", "Статус", "Автор", "Создана"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton refreshButton = new JButton("Обновить");
        JButton addButton = new JButton("Добавить задачу");
        JButton deleteButton = new JButton("Удалить задачу");
        deleteButton.setEnabled("ADMIN".equalsIgnoreCase(role));

        refreshButton.addActionListener(e -> loadTasks());
        addButton.addActionListener(e -> openAddDialog());
        deleteButton.addActionListener(e -> deleteSelectedTask());

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadTasks();
    }

    private void loadTasks() {
        try {
            HttpResult res = sendRequest(baseUrl + "/api/tasks", "GET", null, null, null);
            if (res.status == 200) {
                parseTasks(res.body);
                renderTable();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка загрузки задач: HTTP " + res.status,
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка сети: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddDialog() {
        JTextField titleField = new JTextField();
        JTextField descField = new JTextField();
        JTextField categoryField = new JTextField("Мероприятие");
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"LOW", "NORMAL", "HIGH"});
        priorityBox.setSelectedItem("NORMAL");
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"TODO", "IN_PROGRESS", "DONE"});

        JPanel form = new JPanel(new GridLayout(5, 2, 6, 6));
        form.add(new JLabel("Название:"));
        form.add(titleField);
        form.add(new JLabel("Описание:"));
        form.add(descField);
        form.add(new JLabel("Категория:"));
        form.add(categoryField);
        form.add(new JLabel("Приоритет:"));
        form.add(priorityBox);
        form.add(new JLabel("Статус:"));
        form.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this, form, "Новая задача",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        String title = titleField.getText().trim();
        String category = categoryField.getText().trim();
        if (title.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заполните название и категорию", "Проверка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String[] csrf = fetchCsrf(baseUrl);
            String json = "{\"title\":\"" + escapeJson(title)
                    + "\",\"description\":\"" + escapeJson(descField.getText().trim())
                    + "\",\"priority\":\"" + priorityBox.getSelectedItem()
                    + "\",\"category\":\"" + escapeJson(category)
                    + "\",\"status\":\"" + statusBox.getSelectedItem() + "\"}";
            HttpResult res = sendRequest(baseUrl + "/api/tasks", "POST", json, csrf[0], csrf[1]);
            if (res.status == 201 || res.status == 200) {
                loadTasks();
            } else {
                JOptionPane.showMessageDialog(this, "Не удалось создать задачу: HTTP " + res.status,
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка сети: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedTask() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Выберите задачу в таблице", "Удаление", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        long id = Long.parseLong(tableModel.getValueAt(row, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Удалить задачу #" + id + "?",
                "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            String[] csrf = fetchCsrf(baseUrl);
            HttpResult res = sendRequest(baseUrl + "/api/tasks/" + id, "DELETE", null, csrf[0], csrf[1]);
            if (res.status == 204 || res.status == 200) {
                loadTasks();
            } else {
                JOptionPane.showMessageDialog(this, "Удаление запрещено или ошибка: HTTP " + res.status,
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ошибка сети: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    static String[] fetchCsrf(String baseUrl) throws Exception {
        HttpResult res = sendRequest(baseUrl + "/api/auth/csrf", "GET", null, null, null);
        String header = extractString(res.body, "headerName");
        String token = extractString(res.body, "token");
        return new String[]{header != null ? header : "X-CSRF-TOKEN", token != null ? token : ""};
    }

    static HttpResult sendRequest(String urlStr, String method, String jsonBody,
                                  String csrfHeader, String csrfToken) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod(method);
        if (csrfHeader != null && csrfToken != null) {
            conn.setRequestProperty(csrfHeader, csrfToken);
        }
        if (jsonBody != null) {
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            }
        }
        int status = conn.getResponseCode();
        InputStream stream = status >= 400 ? conn.getErrorStream() : conn.getInputStream();
        StringBuilder sb = new StringBuilder();
        if (stream != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            }
        }
        return new HttpResult(status, sb.toString());
    }

    private void parseTasks(String json) {
        items.clear();
        String trimmed = json.trim();
        if (!trimmed.startsWith("[") || trimmed.equals("[]")) {
            return;
        }
        int depth = 0;
        int start = -1;
        boolean inString = false;
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == '"' && (i == 0 || trimmed.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            if (inString) continue;
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    String obj = trimmed.substring(start, i + 1);
                    String head = obj.contains("\"subtasks\"") ? obj.substring(0, obj.indexOf("\"subtasks\"")) : obj;
                    long id = extractLong(head, "id");
                    String title = nonNull(extractString(head, "title"));
                    String description = nonNull(extractString(head, "description"));
                    String category = nonNull(extractString(head, "category"));
                    String priority = nonNull(extractString(head, "priority"));
                    String status = nonNull(extractString(head, "status"));
                    String owner = nonNull(extractString(head, "owner"));
                    String createdAt = nonNull(extractString(head, "createdAt"));
                    if (createdAt.length() >= 19) createdAt = createdAt.substring(0, 19).replace('T', ' ');
                    items.add(new TaskItem(id, title, description, category, priority, status, owner, createdAt));
                    start = -1;
                }
            }
        }
    }

    private void renderTable() {
        List<TaskItem> sorted = new ArrayList<>(items);
        int mode = sortBox.getSelectedIndex();
        if (mode == 0) {
            sorted.sort(Comparator.comparing((TaskItem t) -> t.createdAt).reversed());
        } else if (mode == 1) {
            sorted.sort(Comparator.comparingInt((TaskItem t) -> priorityRank(t.priority)).reversed());
        } else if (mode == 2) {
            sorted.sort(Comparator.comparing((TaskItem t) -> t.status));
        } else if (mode == 3) {
            sorted.sort(Comparator.comparing((TaskItem t) -> t.title, String.CASE_INSENSITIVE_ORDER));
        }
        tableModel.setRowCount(0);
        for (TaskItem t : sorted) {
            tableModel.addRow(new Object[]{
                    t.id, t.title, t.description, t.category,
                    t.priority, t.status, t.owner, t.createdAt
            });
        }
    }

    private static int priorityRank(String priority) {
        if ("HIGH".equals(priority)) return 3;
        if ("NORMAL".equals(priority)) return 2;
        return 1;
    }

    private static String nonNull(String s) {
        return s == null ? "" : s;
    }

    static String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    static String extractString(String json, String key) {
        Matcher m = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").matcher(json);
        return m.find() ? m.group(1).replace("\\\"", "\"").replace("\\\\", "\\") : null;
    }

    private static long extractLong(String json, String key) {
        Matcher m = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(\\d+)").matcher(json);
        return m.find() ? Long.parseLong(m.group(1)) : 0L;
    }
}
