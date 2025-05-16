package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component("propertyProvider")
@PropertySource("application.properties")
public class PropertyProvider {
    private String filename;

    public String getFileName(){
            return filename;
    }
}