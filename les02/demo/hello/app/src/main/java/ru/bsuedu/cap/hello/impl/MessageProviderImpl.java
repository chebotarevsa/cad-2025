package ru.bsuedu.cap.hello.impl;

import org.springframework.stereotype.Component;
import ru.bsuedu.cap.hello.MessageProvider;

@Component // Говорим Spring: "Создай этот бин"
public class MessageProviderImpl implements MessageProvider {
    @Override
    public String getMessage() {
        return "Hello from Spring DI (Constructor Injection)!";
    }
}