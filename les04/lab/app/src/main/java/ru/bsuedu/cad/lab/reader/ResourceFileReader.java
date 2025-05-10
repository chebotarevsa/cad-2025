package ru.bsuedu.cad.lab.reader;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.time.LocalDateTime;

@Component
public class ResourceFileReader implements Reader {

    @Value("${input.file.name}")
    private String fileName;

    @Override
    public String read() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new RuntimeException("Файл не найден: " + fileName);
        }

        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    @PostConstruct
    public void afterInit() {
        System.out.println("ResourceFileReader инициализирован: " + LocalDateTime.now());
    }
}
