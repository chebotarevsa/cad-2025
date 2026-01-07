package ru.bsuedu.cad.sem2.lab5.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginForm() {
        // UI компоненты
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        // Поля ввода для логина
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JPanel()); // Пустое место
        add(loginButton);

        // Обработчик авторизации
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Попытка авторизации
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String encodedAuth = Base64.getEncoder()
                                           .encodeToString((username + ":" + password)
                                           .getBytes());

                // Если авторизация успешна, открываем главную форму
                TaskManagerForm taskManagerForm = new TaskManagerForm(encodedAuth);
                taskManagerForm.setVisible(true);
                dispose(); // Закрываем форму авторизации
                System.out.println("Encoded Auth: " + encodedAuth);
            }
        });
    }
}
