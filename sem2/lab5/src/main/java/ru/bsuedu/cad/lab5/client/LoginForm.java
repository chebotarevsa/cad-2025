package ru.bsuedu.cad.lab5.client;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginForm extends JFrame {

	private final JTextField usernameField = new JTextField();
	private final JPasswordField passwordField = new JPasswordField();

	public LoginForm() {
		setTitle("Вход");
		setSize(320, 160);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
		panel.add(new JLabel("Логин:"));
		panel.add(usernameField);
		panel.add(new JLabel("Пароль:"));
		panel.add(passwordField);
		panel.add(new JLabel());

		JButton loginButton = new JButton("Войти");
		loginButton.addActionListener(event -> login());
		panel.add(loginButton);
		add(panel);
	}

	private void login() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());
		TaskClient client = new TaskClient(username, password);

		try {
			client.getTasks();
			new TaskManagerForm(client, username).setVisible(true);
			dispose();
		} catch (Exception exception) {
			JOptionPane.showMessageDialog(this, exception.getMessage(), "Ошибка",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
	}
}
