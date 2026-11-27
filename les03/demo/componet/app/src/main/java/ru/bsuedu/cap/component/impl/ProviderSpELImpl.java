package ru.bsuedu.cap.component.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bsuedu.cap.component.Provider;

@Component("providerSpEL")
public class ProviderSpELImpl implements Provider {
    
    @Value("#{property.name}")
    private String appName;

    @Value("#{property.version}")
    private String appVersion;

    @Value("#{property.name} - #{property.version} by #{property.author}")
    private String appFullInfo;

    @Value("#{T(String).join('---',property.numList)}")
    private String join;

    @Value("#{property.fileName}")
    private String filename;

    @Override
    public String getMessage() {
        return "App Name: " + appName + "\n" +
        "App Version: " + appVersion + "\n" +
        "Full Info: " + appFullInfo + "\n" +
        "File name from application.properties: " + filename + "\n" +
        "Sum: " + join + "\n";
    }
}