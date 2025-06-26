package ru.bsuedu.cad.lab;

import java.util.List;

public interface Provider <T>{
    List<T> getItems(Class<T> clazz, int indexFile);
}
