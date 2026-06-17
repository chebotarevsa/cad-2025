package lab1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class ResourceFileReader implements Reader {

    // SpEL можно так, но по сути это просто @Value:
    @Value("#{ '${products.file}' }")
    private String fileName;

    @Override
    public String read() {
        StringBuilder sb = new StringBuilder();
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения файла: " + fileName, e);
        }
        return sb.toString();
    }

    @PostConstruct
    public void onInit() {
        System.out.println("ResourceFileReader полностью инициализирован: " + LocalDateTime.now());
    }
}
