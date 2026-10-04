package ru.bsuedu.cad.lab5.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TaskManagerForm extends JFrame {

	private final TaskClient client;
	private final DefaultTableModel tableModel;
	private final JTable taskTable;
	private final JTextField titleField = new JTextField();
	private final JTextField descriptionField = new JTextField();
	private final JComboBox<String> priorityBox = new JComboBox<>(
			new String[] {"Низкий", "Средний", "Высокий"});
	private final JTextField categoryField = new JTextField();

	public TaskManagerForm(TaskClient client, String username) {
		this.client = client;

		setTitle("Менеджер задач - " + username);
		setSize(900, 450);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(8, 8));

		String[] columns = {"ID", "Название", "Описание", "Статус", "Приоритет",
				"Категория", "Дата создания"};
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		taskTable = new JTable(tableModel);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
		taskTable.setRowSorter(sorter);
		sorter.setSortKeys(List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
		add(new JScrollPane(taskTable), BorderLayout.CENTER);

		JPanel fields = new JPanel(new GridLayout(2, 4, 5, 5));
		fields.add(new JLabel("Название"));
		fields.add(new JLabel("Описание"));
		fields.add(new JLabel("Приоритет"));
		fields.add(new JLabel("Категория"));
		fields.add(titleField);
		fields.add(descriptionField);
		fields.add(priorityBox);
		fields.add(categoryField);

		JButton addButton = new JButton("Добавить");
		addButton.addActionListener(event -> addTask());
		JButton refreshButton = new JButton("Обновить");
		refreshButton.addActionListener(event -> loadTasks());
		JButton deleteButton = new JButton("Удалить");
		deleteButton.setEnabled("admin".equals(username));
		deleteButton.addActionListener(event -> deleteTask());

		JPanel buttons = new JPanel();
		buttons.add(addButton);
		buttons.add(refreshButton);
		buttons.add(deleteButton);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(fields, BorderLayout.CENTER);
		bottomPanel.add(buttons, BorderLayout.SOUTH);
		add(bottomPanel, BorderLayout.SOUTH);

		loadTasks();
	}

	private void loadTasks() {
		try {
			tableModel.setRowCount(0);
			for (TaskDto task : client.getTasks()) {
				String createdAt = task.getCreatedAt() == null
						? "" : task.getCreatedAt().replace('T', ' ');
				tableModel.addRow(new Object[] {
						task.getId(), task.getTitle(), task.getDescription(),
						task.isCompleted() ? "Выполнена" : "Не выполнена",
						task.getPriority(), task.getCategory(), createdAt
				});
			}
		} catch (Exception exception) {
			showError(exception);
		}
	}

	private void addTask() {
		if (titleField.getText().isBlank() || categoryField.getText().isBlank()) {
			JOptionPane.showMessageDialog(this, "Заполните название и категорию");
			return;
		}

		try {
			client.addTask(titleField.getText(), descriptionField.getText(),
					(String) priorityBox.getSelectedItem(), categoryField.getText());
			titleField.setText("");
			descriptionField.setText("");
			categoryField.setText("");
			loadTasks();
		} catch (Exception exception) {
			showError(exception);
		}
	}

	private void deleteTask() {
		int selectedRow = taskTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Выберите задачу");
			return;
		}

		int answer = JOptionPane.showConfirmDialog(this, "Удалить выбранную задачу?",
				"Подтверждение", JOptionPane.YES_NO_OPTION);
		if (answer != JOptionPane.YES_OPTION) {
			return;
		}

		int modelRow = taskTable.convertRowIndexToModel(selectedRow);
		Long id = ((Number) tableModel.getValueAt(modelRow, 0)).longValue();
		try {
			client.deleteTask(id);
			loadTasks();
		} catch (Exception exception) {
			showError(exception);
		}
	}

	private void showError(Exception exception) {
		JOptionPane.showMessageDialog(this, exception.getMessage(), "Ошибка",
				JOptionPane.ERROR_MESSAGE);
	}
}
