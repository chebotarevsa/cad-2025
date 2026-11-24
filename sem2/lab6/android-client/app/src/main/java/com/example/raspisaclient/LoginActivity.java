package com.example.raspisaclient;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        Button openSiteButton = findViewById(R.id.openSiteButton);

        loginButton.setOnClickListener(v -> doLogin());
        openSiteButton.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ScheduleWebActivity.class)));
    }

    private void doLogin() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString();
        TaskApi api = ApiClient.getApi();

        api.getCsrf().enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> res) {
                if (!res.isSuccessful() || res.body() == null) {
                    Toast.makeText(LoginActivity.this, "Ошибка CSRF", Toast.LENGTH_SHORT).show();
                    return;
                }
                String token = res.body().get("token");
                Map<String, String> body = new HashMap<>();
                body.put("login", username);
                body.put("password", password);

                api.login(token, body).enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> loginRes) {
                        if (loginRes.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, TaskManagerActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Сервер недоступен: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
