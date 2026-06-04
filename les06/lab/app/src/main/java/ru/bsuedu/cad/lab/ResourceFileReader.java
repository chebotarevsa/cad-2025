package ru.bsuedu.cad.lab;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class ResourceFileReader implements Reader {

    @Value("${product.file}")
    private String filename;

    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader инициализирован: " + LocalDateTime.now());
    }

    @Override
    public String read() {
        try {
            ClassPathResource resource = new ClassPathResource(filename);
            InputStream is = resource.getInputStream();
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать файл: " + filename, e);
        }
    }
}
