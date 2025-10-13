package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ResourceFileReader implements Reader {
    @Value("${app.products.file:products.csv}")
    private String filePath;

    @PostConstruct
    public void init() {
        System.out.println("ResourceFileReader initialized at " + new Date());
    }

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