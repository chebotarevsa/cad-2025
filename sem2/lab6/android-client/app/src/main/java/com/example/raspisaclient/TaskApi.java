package com.example.raspisaclient;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TaskApi {
    @GET("/api/auth/csrf")
    Call<Map<String, String>> getCsrf();

    @POST("/api/auth/login")
    Call<Map<String, Object>> login(
            @Header("X-CSRF-TOKEN") String csrfToken,
            @Body Map<String, String> body
    );

    @GET("/api/tasks")
    Call<List<Task>> getTasks();
}
