package com.example;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        User user = new User("Иван", 30);

        JAXBContext context = JAXBContext.newInstance(User.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        File file = new File("user.xml");
        marshaller.marshal(user, file);
        System.out.println("XML создан: " + file.getAbsolutePath());

        Unmarshaller unmarshaller = context.createUnmarshaller();
        User result = (User) unmarshaller.unmarshal(file);
        System.out.println("Прочитан из XML: " + result.getName() + ", " + result.getAge());
    }
}