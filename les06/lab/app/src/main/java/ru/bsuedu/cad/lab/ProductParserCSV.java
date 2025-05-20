package ru.bsuedu.cad.lab;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class ProductParserCSV extends CSVParser<Product> {

	@Override
	public Product parseRow(String[] elements) {
		return new Product(
				Integer.parseInt(elements[0]),
				elements[1],
				elements[2],
				Integer.parseInt(elements[3]),
				new BigDecimal(elements[4]),
				Integer.parseInt(elements[5]),
				elements[6],
				convertToDate(elements[7]),
				convertToDate(elements[8]));
	}
}