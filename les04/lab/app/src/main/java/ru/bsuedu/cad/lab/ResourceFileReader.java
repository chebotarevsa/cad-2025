
package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ResourceFileReader implements Reader  {
    private String filePath;

    @Override
    public String read() {
        ClassPathResource resource = new ClassPathResource(filePath);
        try {
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (IOException e) {
            return "";
        }
    }

    ResourceFileReader(FilePathProvider path){
        filePath = path.getFileName();
    }

    @PostConstruct
    private void init() {
        System.out.println(LocalDateTime.now());
    }
    
}
