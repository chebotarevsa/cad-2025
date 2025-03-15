package ru.bsuedu.cad.lab;

import java.util.List;

public interface Parser<T> {
    List<T> parse(String text);
} 
