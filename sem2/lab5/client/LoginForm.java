import javax.swing.*;
import java.awt.*;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class LoginForm extends JFrame {
    private static final String BASE_URL = "http://localhost:8085";
    private final JTextField usernameField = new JTextField("admin");
    private final JPasswordField passwordField = new JPasswordField("Admin!2026");
    private final JButton loginButton = new JButton("Войти");

    public LoginForm() {
        setTitle("Вход – Raspisa Client");
        setSize(340, 170);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        panel.add(new JLabel("Логин:"));
        panel.add(usernameField);
        panel.add(new JLabel("Пароль:"));
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);
        add(panel);

        loginButton.addActionListener(e -> login());
        getRootPane().setDefaultButton(loginButton);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите логин и пароль", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            String[] csrf = TaskManagerForm.fetchCsrf(BASE_URL);
            String body = "{\"login\":\"" + TaskManagerForm.escapeJson(username) + "\",\"password\":\""
                    + TaskManagerForm.escapeJson(password) + "\"}";
            TaskManagerForm.HttpResult res = TaskManagerForm.sendRequest(
                    BASE_URL + "/api/auth/login", "POST", body, csrf[0], csrf[1]);

            if (res.status == 200) {
                String role = TaskManagerForm.extractString(res.body, "role");
                TaskManagerForm form = new TaskManagerForm(BASE_URL, username, role != null ? role : "");
                form.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Неверный логин или пароль (" + res.status + ")",
                        "Ошибка входа", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Нет связи с сервером: " + ex.getMessage(),
                    "Ошибка сети", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
