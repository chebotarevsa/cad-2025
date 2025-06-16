package ru.bsuedu.cad.lab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component("reader")
public class ResourceFileReader implements Reader {
   
   @PostConstruct
   public void init() {
      Date cur = new Date();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      String currentDateTime = dateFormat. format(cur);
      System.out.println(currentDateTime);
   }

   //@Value("#{propertyProvider.fileName}")
   private String path;

   public ResourceFileReader(PropertyProvider propertyProvider) {
      path = propertyProvider.getFileName();
   }

   public String read() {
      Resource resource = new ClassPathResource("product.csv");

      try {
         return new String(Files.readAllBytes(Paths.get(resource.getURI())));
      } catch (FileNotFoundException var3) {
         var3.printStackTrace();
         return null;
      } catch (IOException var4) {
         var4.printStackTrace();
         return null;
      }
   }
}