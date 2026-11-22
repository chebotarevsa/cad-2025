import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class LoginForm extends JFrame {

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton loginButton = new JButton("Войти");

    public LoginForm() {
        setTitle("Вход");
        setSize(360, 230);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel header = new JLabel("Task Manager", SwingConstants.CENTER);
        header.setFont(UiStyle.BOLD.deriveFont(20f));
        header.setForeground(UiStyle.PINK_DARK);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Имя пользователя:"));
        form.add(usernameField);
        form.add(new JLabel("Пароль:"));
        form.add(passwordField);
        form.add(new JPanel()); // пустое место
        form.add(loginButton);

        JLabel hint = new JLabel("Учётные записи: user, moderator, admin", SwingConstants.CENTER);
        hint.setForeground(UiStyle.GRAY);

        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBorder(BorderFactory.createEmptyBorder(14, 18, 12, 18));
        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(hint, BorderLayout.SOUTH);
        setContentPane(root);

        // Enter в любом поле = нажатие кнопки
        getRootPane().setDefaultButton(loginButton);
        loginButton.addActionListener(e -> login());
    }

    /** Проверяет логин/пароль запросом к серверу и только после этого открывает главное окно. */
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите имя пользователя и пароль",
                    "Вход", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ApiClient client = new ApiClient(ApiClient.encodeCredentials(username, password));
        loginButton.setEnabled(false);

        new SwingWorker<ApiClient.UserInfo, Void>() {
            @Override
            protected ApiClient.UserInfo doInBackground() throws Exception {
                return client.me();
            }

            @Override
            protected void done() {
                loginButton.setEnabled(true);
                try {
                    ApiClient.UserInfo user = get();
                    TaskManagerForm form = new TaskManagerForm(client, user);
                    form.setVisible(true);
                    dispose();
                } catch (ExecutionException e) {
                    passwordField.setText("");
                    JOptionPane.showMessageDialog(LoginForm.this, e.getCause().getMessage(),
                            "Ошибка входа", JOptionPane.ERROR_MESSAGE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UiStyle.install();
            new LoginForm().setVisible(true);
        });
    }
}
