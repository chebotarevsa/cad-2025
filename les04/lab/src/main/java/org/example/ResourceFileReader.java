package org.example;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class ResourceFileReader implements Reader {

    // Путь к CSV файлу
    private final String filePath =
            "C:/les03/lab/src/main/resources/products.csv";

    public ResourceFileReader() {
        System.out.println("ResourceFileReader создан: " + LocalDateTime.now());
    }

    @Override
    public String read() {

        StringBuilder result = new StringBuilder();

        try (
                InputStream inputStream = new FileInputStream(filePath);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                inputStream,
                                StandardCharsets.UTF_8
                        )
                )
        ) {

            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

        } catch (Exception e) {

            System.out.println("Файл не найден: " + filePath);
            System.out.println("Ошибка чтения файла: " + e.getMessage());

        }

        return result.toString();
    }
}