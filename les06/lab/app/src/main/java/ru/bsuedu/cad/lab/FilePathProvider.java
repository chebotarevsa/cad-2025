package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("application.properties")
public class FilePathProvider {
    @Value("${filename.product}")
    private String fileNameProduct;

    @Value("${filename.category}")
    private String fileNameCategory;

    public String getFileNameCategory() {
        return fileNameCategory;
    }

    public String getFileNameProduct(){
        return fileNameProduct;
    }
}
