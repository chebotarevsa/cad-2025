package ru.bsuedu.cap.hello.impl;


import ru.bsuedu.cap.hello.MessageProvider;
import ru.bsuedu.cap.hello.MessageRenderer;

public class MessageRendererImpl implements MessageRenderer {

    private MessageProvider messageProvider;

    @Override
    public void render() {
        if(messageProvider ==null) throw new RuntimeException("You mast set property in class " + this.getClass().getName());
        System.out.println(messageProvider.getMessage());
    }

    @Override
    public void setMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @Override
    public MessageProvider getMessageProvider() {
        return this.messageProvider;
    }
    
}
