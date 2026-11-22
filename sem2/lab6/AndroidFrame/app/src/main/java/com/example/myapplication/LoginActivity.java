package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Подключаем XML макет

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String encodedAuth = Base64.encodeToString(
                        (username + ":" + password).getBytes(), Base64.NO_WRAP);

                // Переход на TaskManagerActivity
                Intent intent = new Intent(LoginActivity.this, TaskManagerActivity.class);
                intent.putExtra("authHeader", encodedAuth); // Передаем токен в Intent
                startActivity(intent);
            }
        });
    }
}
