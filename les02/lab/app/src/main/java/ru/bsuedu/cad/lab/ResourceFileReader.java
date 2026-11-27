package ru.bsuedu.cad.lab;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceFileReader implements Reader {
    private final String filePath;

    public ResourceFileReader(String filePath) {
        this.filePath = filePath;
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