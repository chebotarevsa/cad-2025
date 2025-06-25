package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("application.properties")
public class FilePathProvider {
    @Value("${filename}") // реализация SPel для файла application.properties через переменную filename
    private String fileName;

    public String getFileName(){
        return fileName;
    }
}
