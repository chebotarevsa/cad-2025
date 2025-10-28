package ru.bsuedu.cad.lab;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component("reader")
public class ResourceFileReader implements Reader {

    private String path;

    public ResourceFileReader(@Value("${product.file.name}") String fileName) {
        try {
            path = Paths.get(ClassLoader.getSystemResource(fileName).toURI()).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка загрузки файла resources/" + fileName, e);
        }
    }

    public String read() {
        try (FileReader reader = new FileReader(path)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }

            return result.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostConstruct
    public void init() {
        LocalDateTime now = LocalDateTime.now();
        String formatted = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("ResourceFileReader initialized: " + formatted);
    }
}
