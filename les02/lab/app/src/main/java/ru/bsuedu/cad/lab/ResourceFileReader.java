package ru.bsuedu.cad.lab;

import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceFileReader implements Reader {

    @Override
    public String read() {
        try {
            ClassPathResource resource = new ClassPathResource("product.csv");
            InputStream is = resource.getInputStream();
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать product.csv", e);
        }
    }
}
