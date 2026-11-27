package ru.bsuedu.cap.component.impl;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Component("property")
public class Property {
    private final String version = "1.0.0";
    private final String name = "Component";
    private final String author = "Den";
    private final List<String> numList = Arrays.asList("1", "2", "3", "4", "5");

    public String getVersion() { return version; }
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public List<String> getNumList() { return numList; }

    public String getFileName() {
        Properties prop = new Properties();
        try {
            prop.load(Property.class.getClassLoader().getResourceAsStream("application.properties"));
            return prop.getProperty("filename");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}