package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
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

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private String authHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Получаем токен авторизации из LoginActivity
        authHeader = getIntent().getStringExtra("authHeader");

        loadTasks();
    }

    private void loadTasks() {
        // Используем Retrofit для обращения к API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // localhost для эмулятора
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TaskApi taskApi = retrofit.create(TaskApi.class);
        Call<List<Task>> call = taskApi.getTasks("Basic " + authHeader);

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Передаем данные в адаптер для отображения
                    List<Task> tasks = response.body();
                    taskAdapter = new TaskAdapter(tasks);
                    recyclerView.setAdapter(taskAdapter);
                } else {
                    Toast.makeText(TaskManagerActivity.this,
                            "Ошибка загрузки задач", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("TaskManagerActivity", "Ошибка загрузки", t);
            }
        });
    }
}
