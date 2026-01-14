package ru.bsuedu.cad_2025;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {
    private List<String> userMessages = new ArrayList<>();

    @GetMapping("/")
    public String helloWorld(){
        return "Hello, World!";
    }
    
    @GetMapping("/messages")
    public List<String> getAllMessages() {
        return userMessages;
    }

    @GetMapping("/messages/{index}")
    public String getMessageByIndex(@PathVariable int index){
        return userMessages.get(index);
    }
    
    @PostMapping("/messages")
    public String publishMessage(@RequestBody String message) {
        if (message.length() == 0 || message == " ")
            return "Message is empty";
        else
            userMessages.add(message);
        return "Message published successfully!";
    }

    @DeleteMapping("/messages")
    public String clearAllMessages() {
        userMessages.clear();
        return "Messages was cleared";
    }
    
    @PutMapping("/messages/{index}")
    public String updateMessage(@PathVariable int index, @RequestBody String message) {
        if (index >= 0 && index < userMessages.size())
        {
            userMessages.set(index, message);
            return "Message updated successfully!";
        }
        return "Message not found at index " + index;
    }
}
