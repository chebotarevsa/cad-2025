package ru.bsuedu.cad.lab5.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskClient {

	private static final String API_URL = System.getProperty(
			"task.server.url", "http://localhost:8080/api");

	private final HttpClient httpClient = HttpClient.newHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	private final String authorization;

	public TaskClient(String username, String password) {
		String loginAndPassword = username + ":" + password;
		authorization = "Basic " + Base64.getEncoder()
				.encodeToString(loginAndPassword.getBytes(StandardCharsets.UTF_8));
	}

	public List<TaskDto> getTasks() throws IOException, InterruptedException {
		HttpRequest request = requestBuilder(API_URL).GET().build();
		HttpResponse<String> response = httpClient.send(request,
				HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
		checkResponse(response);
		return objectMapper.readValue(response.body(), new TypeReference<List<TaskDto>>() { });
	}

	public void addTask(String title, String description, String priority, String category)
			throws IOException, InterruptedException {
		TaskDto task = new TaskDto();
		task.setTitle(title);
		task.setDescription(description);
		task.setPriority(priority);
		task.setCategory(category);

		HttpRequest request = requestBuilder(API_URL)
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(task)))
				.build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		checkResponse(response);
	}

	public void deleteTask(Long id) throws IOException, InterruptedException {
		HttpRequest request = requestBuilder(API_URL + "/" + id).DELETE().build();
		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		checkResponse(response);
	}

	private HttpRequest.Builder requestBuilder(String url) {
		return HttpRequest.newBuilder(URI.create(url))
				.header("Authorization", authorization);
	}

	private void checkResponse(HttpResponse<String> response) throws IOException {
		if (response.statusCode() == 401) {
			throw new IOException("Неверный логин или пароль");
		}
		if (response.statusCode() == 403) {
			throw new IOException("Недостаточно прав для выполнения операции");
		}
		if (response.statusCode() < 200 || response.statusCode() >= 300) {
			throw new IOException("Сервер вернул ошибку " + response.statusCode());
		}
	}
}
