package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component("propertyProvider")
@PropertySource("application.properties")
public class PropertyProvider {
    
    @Value("${filename.product}")
    private String fileNameProduct;
    
    @Value("${filename.category}")
    private String fileNameCategory;


    public String[] getFileName(){
        
        return new String[] {fileNameProduct, fileNameCategory};
    }
}
