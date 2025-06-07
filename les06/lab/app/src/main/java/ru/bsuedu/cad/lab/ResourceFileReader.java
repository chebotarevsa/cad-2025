package ru.bsuedu.cad.lab;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при чтении файла", e);
        }
    }
}
