package ru.bsuedu.cap.hello.impl;

import ru.bsuedu.cap.hello.MessageProvider;

public class MessageProviderImpl implements MessageProvider{

    @Override
    public String getMessage() {
        return "Hello, world!";
    }

}
