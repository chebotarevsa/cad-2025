package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component("propertyProvider")
@PropertySource("application.properties")
public class PropertyProvider {
    
    @Value("${filename}")
    private String fileName;

    public String getFileName(){
        return fileName;
    }
}
