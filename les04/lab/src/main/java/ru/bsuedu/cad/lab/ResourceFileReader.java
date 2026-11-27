package ru.bsuedu.cad.lab;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResourceFileReader implements Reader {

    @Value("${product.file.name}")
    private String filePath;

    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader инициализирован: " + LocalDateTime.now());
    }

    @Override
    public List<String> readAllLines() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла: " + filePath, e);
        }
    }
}