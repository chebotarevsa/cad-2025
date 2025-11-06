package ru.bsuedu.cad.lab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;


@Component("reader")
public class ResourceFileReader implements Reader {

    // SPeL
    // @Value("#{propertyProvider.fileName}")
    private String[] path;

    // Конструктор
    public ResourceFileReader(PropertyProvider propertyProvider){
        path = propertyProvider.getFileName();
    }

    

    @PostConstruct
    public void init() {
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        String currentDateTime = dateFormat.format(currentTime);
        System.out.println(currentDateTime);
    }

    public String read(int indexFile){
        
        Resource resource = new ClassPathResource(path[indexFile]);
        
        try {
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));

            // StringBuffer text = new StringBuffer("");
            // String line;
            
            // while (true){

            //     line = buffReader.readLine();
            //     if (line == null) break;
            //     text.append(line);
            //     text.append("\n");

            // }
            
            
            // return text.toString();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
        
        
    } 

    
}
