package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Объект в JSON
        User user = new User("Иван", 30);
        String json = mapper.writeValueAsString(user);
        System.out.println("Сериализация в JSON:");
        System.out.println(json);

        // JSON обратно в объект
        User deserialized = mapper.readValue(json, User.class);
        System.out.println("Десериализация из JSON:");
        System.out.println(deserialized.getName() + ", " + deserialized.getAge());

        // Работа с коллекцией
        String jsonArray = "[{"name":"Анна","age":25},{"name":"Олег","age":40}]";
        List<User> users = mapper.readValue(jsonArray, new TypeReference<List<User>>() {});
        System.out.println("Десериализация массива пользователей:");
        for (User u : users) {
            System.out.println(u.getName() + ", " + u.getAge());
        }
    }
}