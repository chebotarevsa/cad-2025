package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Message {
    private String text;
    private LocalDateTime timestamp;

    public Message(String text) {
        this.text = text;
        this.timestamp = LocalDateTime.now();
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

@RestController
public class MessageController {
    private static final int MAX_MESSAGES = 100;
    private List<Message> userMessages = new ArrayList<>();

  @GetMapping("/1")
    public String helloWorld() {
      return "Hello, World!";
 }

    @GetMapping("/messages")
    public List<String> getAllMessages() {
        return userMessages.stream().map(Message::getText).collect(Collectors.toList());
    }

    @PostMapping("/messages")
    public String addMessage(@RequestBody(required = false) String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Error: Message cannot be empty";
        }
        userMessages.add(new Message(message));
        if (userMessages.size() > MAX_MESSAGES) {
            userMessages.remove(0);
        }
        return "Message added successfully";
    }

    @GetMapping("/messages/{index}")
    public String getMessageByIndex(@PathVariable int index) {
        if (index >= 0 && index < userMessages.size()) {
            return userMessages.get(index).getText();
        }
        return "Message not found at index " + index;
    }

    @DeleteMapping("/messages/clear")
    public String clearAllMessages() {
        userMessages.clear();
        return "All messages cleared successfully";
    }

    @GetMapping("/messages/count")
    public int countMessages() {
        return userMessages.size();
    }

    @GetMapping("/messages/after")
    public List<String> getMessagesAfter(@RequestParam String dateTime) {
        LocalDateTime time = LocalDateTime.parse(dateTime);
        return userMessages.stream()
                .filter(m -> m.getTimestamp().isAfter(time))
                .map(Message::getText)
                .collect(Collectors.toList());
    }

    @PutMapping("/messages/{index}")
    public String updateMessage(@PathVariable int index, @RequestBody(required = false) String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Error: Message cannot be empty";
        }
        if (index >= 0 && index < userMessages.size()) {
            userMessages.set(index, new Message(message));
            return "Message updated successfully";
        }
        return "Message not found at index " + index;
    }

    @DeleteMapping("/messages/{index}")
    public String deleteMessage(@PathVariable int index) {
        if (index >= 0 && index < userMessages.size()) {
            userMessages.remove(index);
            return "Message deleted successfully";
        }
        return "Message not found at index " + index;
    }
}
