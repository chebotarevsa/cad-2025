package ru.bsuedu.cad.lab;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ResourceFileReader implements Reader {
    @Override
    public String read() {
        try {
            ClassPathResource resource = new ClassPathResource("products.csv");
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении файла", e);
        }
    }
}