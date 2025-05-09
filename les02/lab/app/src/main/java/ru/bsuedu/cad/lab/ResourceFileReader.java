package ru.bsuedu.cad.lab;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ResourceFileReader implements Reader {
   private String path = "product.csv";

   public ResourceFileReader() {
   }

   public String read() {
      Resource resource = new ClassPathResource(this.path);

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