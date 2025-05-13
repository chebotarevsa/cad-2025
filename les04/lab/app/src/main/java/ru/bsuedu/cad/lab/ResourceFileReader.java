package ru.bsuedu.cad.lab;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Component
public class ResourceFileReader implements Reader {

    @Value("${product.file}")
    private String fileName;

    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader инициализирован: " + LocalDateTime.now());
    }

    @Override
    public String read() {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении файла", e);
        }
    }
}
