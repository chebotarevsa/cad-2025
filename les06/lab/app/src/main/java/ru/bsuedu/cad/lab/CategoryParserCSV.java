package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

@Component
public class CategoryParserCSV extends CSVParser<Category> {

	@Override
	public Category parseRow(String[] elements) {
		return new Category(Integer.parseInt(elements[0]),
				elements[1],
				elements[2]);
	}
}