package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceFileReader implements Reader {
    @Value("${app.products.file:products.csv}")
    private String filePath;

    @Override
    public List<String> read() {
        List<String> lines = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }
}