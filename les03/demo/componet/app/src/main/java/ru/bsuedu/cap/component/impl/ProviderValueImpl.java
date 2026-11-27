package ru.bsuedu.cap.component.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.bsuedu.cap.component.Provider;

@Component("providerValue")
public class ProviderValueImpl  implements Provider{

    @Value("Message from provider value")
    private String message;

    @Value("10")
    private int count;

    @Value("true")
    private boolean isSimple;

    @Override
    public String getMessage() {
        return String.format("Message -  %s, %d, %b", message, count,isSimple);
    }

}


