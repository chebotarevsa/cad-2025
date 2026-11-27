package ru.bsuedu.cap.component.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import ru.bsuedu.cap.component.Provider;

@Component("providerResource")
@PropertySource("application.properties")
public class ProviderResource implements Provider {

    @Value("${filename}")
    private String fileName;

    @Override
    public String getMessage() {
        return  fileName;
    }
    
}
