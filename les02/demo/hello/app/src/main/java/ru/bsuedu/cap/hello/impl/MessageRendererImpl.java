package ru.bsuedu.cap.hello.impl;

import org.springframework.stereotype.Component;
import ru.bsuedu.cap.hello.MessageProvider;
import ru.bsuedu.cap.hello.MessageRenderer;

@Component // Говорим Spring: "Этот класс тоже бин"
public class MessageRendererImpl implements MessageRenderer {

    // Делаем поле final (неизменяемым после создания)
    private final MessageProvider messageProvider;

    // Внедрение через конструктор!
    public MessageRendererImpl(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public void render() {
        // Проверка уже не нужна, Spring не даст создать объект без зависимости
        System.out.println(messageProvider.getMessage());
    }

    // Сеттер больше не нужен для внедрения, но можно оставить, если требует интерфейс
    @Override
    public void setMessageProvider(MessageProvider messageProvider) {
        // Оставьте пустым или удалите, если интерфейс позволяет
    }

    @Override
    public MessageProvider getMessageProvider() {
        return this.messageProvider;
    }
}