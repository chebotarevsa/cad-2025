package ru.bsuedu.cad.lab6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameField = findViewById(R.id.usernameField);
        EditText passwordField = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            String credentials = username + ":" + password;
            String authHeader = "Basic " + Base64.encodeToString(
                    credentials.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);

            Intent intent = new Intent(this, TaskManagerActivity.class);
            intent.putExtra("authHeader", authHeader);
            startActivity(intent);
        });
    }
}
