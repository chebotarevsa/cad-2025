package ru.bsuedu.cad.lab;

import java.util.List;

public interface DataProvider<T> {
	List<T> getItems();
}