import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class TaskManagerForm extends JFrame {

    private static final DateTimeFormatter DATE_VIEW = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // ---- сортировка -------------------------------------------------
    private static final Comparator<TaskItem> BY_TITLE =
            Comparator.comparing(t -> t.title().toLowerCase());
    private static final Comparator<TaskItem> BY_DATE_ASC =
            Comparator.comparing(TaskItem::dueDate, Comparator.nullsLast(Comparator.<LocalDate>naturalOrder()));
    private static final Comparator<TaskItem> BY_DATE_DESC =
            Comparator.comparing(TaskItem::dueDate, Comparator.nullsLast(Comparator.<LocalDate>reverseOrder()));
    private static final Comparator<TaskItem> BY_STATUS =
            Comparator.comparing(TaskItem::completed);

    private enum SortMode {
        NONE("Как на сервере", null),
        TITLE_ASC("По названию (А-Я)", BY_TITLE),
        TITLE_DESC("По названию (Я-А)", BY_TITLE.reversed()),
        DATE_ASC("По сроку: сначала ближайшие", BY_DATE_ASC),
        DATE_DESC("По сроку: сначала дальние", BY_DATE_DESC),
        STATUS("Сначала невыполненные", BY_STATUS);

        final String label;
        final Comparator<TaskItem> comparator;

        SortMode(String label, Comparator<TaskItem> comparator) {
            this.label = label;
            this.comparator = comparator;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // ---- модель таблицы -----------------------------------------------
    private static final String[] COLUMNS = {"ID", "Название", "Описание", "Срок", "Статус"};

    private static final class TaskTableModel extends AbstractTableModel {
        private List<TaskItem> rows = new ArrayList<>();

        void setRows(List<TaskItem> rows) {
            this.rows = rows;
            fireTableDataChanged();
        }

        TaskItem get(int row) {
            return rows.get(row);
        }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return COLUMNS.length; }
        @Override public String getColumnName(int column) { return COLUMNS[column]; }

        @Override
        public Object getValueAt(int row, int column) {
            TaskItem t = rows.get(row);
            return switch (column) {
                case 0 -> t.id();
                case 1 -> t.title();
                case 2 -> t.description();
                case 3 -> t.dueDate() == null ? "-" : t.dueDate().format(DATE_VIEW);
                default -> t.completed() ? "Выполнена" : "Не выполнена";
            };
        }
    }

    private final ApiClient client;
    private final ApiClient.UserInfo user;
    private final boolean canComplete;
    private final boolean canDelete;

    private final TaskTableModel tableModel = new TaskTableModel();
    private final JTable table = new JTable(tableModel);
    private final JComboBox<SortMode> sortBox = new JComboBox<>(SortMode.values());
    private final JButton refreshButton = new JButton("Обновить");
    private final JButton addButton = new JButton("Добавить задачу");
    private final JButton completeButton = new JButton("Отметить выполненной");
    private final JButton deleteButton = new JButton("Удалить");
    private final JLabel statusLabel = new JLabel(" ");

    private List<TaskItem> loaded = new ArrayList<>();
    private boolean busy;

    public TaskManagerForm(ApiClient client, ApiClient.UserInfo user) {
        this.client = client;
        this.user = user;
        // Права: сервер всё равно проверяет их сам, здесь мы только скрываем недоступное
        this.canComplete = user.hasRole("MODERATOR") || user.hasRole("ADMIN");
        this.canDelete = user.hasRole("ADMIN");

        setTitle("Task Manager - " + user.username() + " (" + String.join(", ", user.roles()) + ")");
        setSize(820, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        sortBox.setPrototypeDisplayValue(SortMode.DATE_ASC); // ширина списка по самому длинному пункту
        addButton.addActionListener(e -> addTask());
        completeButton.addActionListener(e -> completeTask());
        deleteButton.addActionListener(e -> deleteTask());
        refreshButton.addActionListener(e -> loadTasks());
        sortBox.addActionListener(e -> applySort());
        table.getSelectionModel().addListSelectionListener(e -> updateButtons());

        if (!canComplete) completeButton.setToolTipText("Доступно ролям MODERATOR и ADMIN");
        if (!canDelete) deleteButton.setToolTipText("Доступно только роли ADMIN");

        updateButtons();
        loadTasks();
    }

    // ---- построение интерфейса ------------------------------------------
    private JPanel createTopPanel() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        top.setBackground(UiStyle.PINK_LIGHT);
        top.add(new JLabel("Сортировка:"));
        top.add(sortBox);
        top.add(refreshButton);
        return top;
    }

    private JScrollPane createTable() {
        table.setRowHeight(28);
        table.setFont(UiStyle.BASE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(UiStyle.BOLD);
        table.setDefaultRenderer(Object.class, new RowRenderer());
        int[] widths = {50, 190, 280, 100, 120};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(java.awt.Color.WHITE);
        return scroll;
    }

    private JPanel createBottomPanel() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        buttons.setBackground(UiStyle.PINK_LIGHT);
        buttons.add(addButton);
        buttons.add(completeButton);
        buttons.add(deleteButton);

        statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 10, 6, 10));
        statusLabel.setForeground(UiStyle.TEXT);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(buttons, BorderLayout.CENTER);
        bottom.add(statusLabel, BorderLayout.SOUTH);
        return bottom;
    }

    /** Строки через одну подкрашены, выполненные задачи серые, статус цветной. */
    private final class RowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected,
                                                       boolean focus, int row, int column) {
            super.getTableCellRendererComponent(table, value, selected, false, row, column);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            TaskItem task = tableModel.get(row);
            if (!selected) {
                setBackground(row % 2 == 0 ? java.awt.Color.WHITE : UiStyle.ROW_ALT);
                setForeground(task.completed() ? UiStyle.GRAY : UiStyle.TEXT);
                if (column == 4) {
                    setForeground(task.completed() ? UiStyle.GREEN : UiStyle.ORANGE);
                }
            } else {
                setForeground(java.awt.Color.WHITE);
            }
            setFont(column == 1 ? UiStyle.BOLD : UiStyle.BASE);
            return this;
        }
    }

    // ---- работа с сервером ------------------------------------------------
    /** Выполняет запрос в фоне, чтобы окно не "зависало" на время ожидания сервера. */
    private <T> void async(Callable<T> job, Consumer<T> onSuccess) {
        busy = true;
        updateButtons();
        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return job.call();
            }

            @Override
            protected void done() {
                busy = false;
                try {
                    onSuccess.accept(get());
                } catch (ExecutionException e) {
                    JOptionPane.showMessageDialog(TaskManagerForm.this, e.getCause().getMessage(),
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                updateButtons();
            }
        }.execute();
    }

    private void loadTasks() {
        async(client::getTasks, tasks -> {
            loaded = tasks;
            applySort();
        });
    }

    private void applySort() {
        List<TaskItem> sorted = new ArrayList<>(loaded);
        SortMode mode = (SortMode) sortBox.getSelectedItem();
        if (mode != null && mode.comparator != null) {
            sorted.sort(mode.comparator);
        }
        tableModel.setRows(sorted);
        statusLabel.setText("Задач: " + sorted.size());
    }

    private void addTask() {
        JTextField title = new JTextField(22);
        JTextField description = new JTextField(22);
        JTextField due = new JTextField(22);
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 4));
        panel.add(new JLabel("Название:"));
        panel.add(title);
        panel.add(new JLabel("Описание:"));
        panel.add(description);
        panel.add(new JLabel("Срок выполнения (гггг-мм-дд, можно не указывать):"));
        panel.add(due);

        while (true) {
            int answer = JOptionPane.showConfirmDialog(this, panel, "Новая задача",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (answer != JOptionPane.OK_OPTION) {
                return;
            }
            if (title.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Название не может быть пустым",
                        "Новая задача", JOptionPane.WARNING_MESSAGE);
                continue;
            }
            LocalDate dueDate = null;
            if (!due.getText().isBlank()) {
                try {
                    dueDate = LocalDate.parse(due.getText().trim());
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this, "Дата должна быть в формате гггг-мм-дд, например 2026-12-31",
                            "Новая задача", JOptionPane.WARNING_MESSAGE);
                    continue;
                }
            }
            String t = title.getText().trim();
            String d = description.getText().trim();
            LocalDate finalDue = dueDate;
            async(() -> {
                client.addTask(t, d, finalDue);
                return client.getTasks();
            }, tasks -> {
                loaded = tasks;
                applySort();
            });
            return;
        }
    }

    private void completeTask() {
        TaskItem task = selectedTask();
        if (task == null) return;
        async(() -> {
            client.setCompleted(task.id(), true);
            return client.getTasks();
        }, tasks -> {
            loaded = tasks;
            applySort();
        });
    }

    private void deleteTask() {
        TaskItem task = selectedTask();
        if (task == null) return;
        int answer = JOptionPane.showConfirmDialog(this,
                "Удалить задачу \"" + task.title() + "\"?", "Подтверждение удаления",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) return;
        async(() -> {
            client.deleteTask(task.id());
            return client.getTasks();
        }, tasks -> {
            loaded = tasks;
            applySort();
        });
    }

    private TaskItem selectedTask() {
        int row = table.getSelectedRow();
        return row < 0 ? null : tableModel.get(row);
    }

    private void updateButtons() {
        TaskItem task = selectedTask();
        refreshButton.setEnabled(!busy);
        addButton.setEnabled(!busy);
        completeButton.setEnabled(!busy && canComplete && task != null && !task.completed());
        deleteButton.setEnabled(!busy && canDelete && task != null);
    }
}
