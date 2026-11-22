package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface TaskApi {
    @GET("/api")
    Call<List<Task>> getTasks(@Header("Authorization") String authHeader);
}
