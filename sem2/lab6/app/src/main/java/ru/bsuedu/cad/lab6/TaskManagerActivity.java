package ru.bsuedu.cad.lab6;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaskManagerActivity extends AppCompatActivity {

    private RecyclerView taskList;
    private TaskApi taskApi;
    private String authHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        taskList = findViewById(R.id.taskList);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        authHeader = getIntent().getStringExtra("authHeader");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        taskApi = retrofit.create(TaskApi.class);

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(view -> loadTasks());
        loadTasks();
    }

    private void loadTasks() {
        taskApi.getTasks(authHeader).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    taskList.setAdapter(new TaskAdapter(response.body()));
                } else if (response.code() == 401) {
                    Toast.makeText(TaskManagerActivity.this,
                            "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TaskManagerActivity.this,
                            "Ошибка загрузки: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable throwable) {
                Toast.makeText(TaskManagerActivity.this,
                        "Сервер недоступен", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
