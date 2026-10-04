package ru.bsuedu.cad.lab1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

	private final List<String> userMessages = new ArrayList<>();

	@GetMapping("/")
	public String helloWorld() {
		return "Привет, мир!";
	}

	@GetMapping("/messages")
	public List<String> getAllMessages() {
		return userMessages;
	}

	@PostMapping("/messages")
	public String publishMessage(@RequestBody String message) {
		userMessages.add(message);
		return "Сообщение успешно опубликовано!";
	}

	@PutMapping("/messages/{index}")
	public String updateMessage(@PathVariable int index, @RequestBody String message) {
		if (index >= 0 && index < userMessages.size()) {
			userMessages.set(index, message);
			return "Сообщение успешно обновлено!";
		}

		return "Сообщение с индексом " + index + " не найдено";
	}

	@DeleteMapping("/messages/{index}")
	public String deleteMessage(@PathVariable int index) {
		if (index >= 0 && index < userMessages.size()) {
			userMessages.remove(index);
			return "Сообщение успешно удалено!";
		}

		return "Сообщение с индексом " + index + " не найдено";
	}

	@DeleteMapping("/messages")
	public String clearAllMessages() {
		userMessages.clear();
		return "Все сообщения успешно удалены!";
	}

	@GetMapping("/messages/count")
	public int countMessages() {
		return userMessages.size();
	}
}
