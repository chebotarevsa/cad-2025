package ru.bsuedu.cad.lab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;

public class ResourceFileReader implements Reader {

    @Override
    public String read() {
            ClassPathResource resource = new ClassPathResource(filePath);
            try {
                return new String(Files.readAllBytes(Paths.get(resource.getURI())));
            } catch (IOException e) {
            
                return "";
                 
            }
    }
    private String filePath = "product.csv";
}
