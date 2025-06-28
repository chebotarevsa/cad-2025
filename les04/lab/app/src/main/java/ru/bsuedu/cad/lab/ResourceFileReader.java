package ru.bsuedu.cad.lab;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;

@Component
public class ResourceFileReader implements Reader {

    private final Resource resource;

    public ResourceFileReader(Resource resource) {
        this.resource = resource;
    }

    @PostConstruct
    public void init()
    {
        Date initializationTime = new Date();
        System.out.println("Бин ResourceFileReader был создан " + new SimpleDateFormat("dd.MM.yyyy в HH:mm:ss").format(initializationTime));
    }

    @Override
    public String read() {
        try {
            return new String(resource.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке ресурса: ", e);
        }
    }

}