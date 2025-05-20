package ru.bsuedu.cad.lab;

import java.util.List;

public interface Request<T> {
	List<T> execute();
}