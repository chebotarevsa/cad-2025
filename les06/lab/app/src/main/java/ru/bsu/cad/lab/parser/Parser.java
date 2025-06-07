package ru.bsu.cad.lab.parser;

import java.util.List;

public interface Parser<T> {  // Добавляем generic параметр <T>
    List<T> parse(String content);
}