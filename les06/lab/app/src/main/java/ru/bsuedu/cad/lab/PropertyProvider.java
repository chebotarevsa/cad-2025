package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component("propertyProvider")
@PropertySource("application.properties")
public class PropertyProvider {

    @Value("${filename.product}")
    private String productFilename;

    @Value("${filename.category}")
    private String categoryFilename;

    public String getProductFileName(){
            return productFilename;
    }

    public String getCategoryFileName(){
        return categoryFilename;
    }
}