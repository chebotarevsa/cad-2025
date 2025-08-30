package ru.bsuedu.cad.lab;

import java.math.BigDecimal;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
//import org.springframework.stereotype.Component;

public class CSVParser implements Parser {

	@Override
	public List<Product> parse(String text) {
		List<Product> products = new ArrayList<>();

		String[] rows = text.split("\n");

		for (int i = 1; i < rows.length; i++) {
			String[] elements = rows[i].split(",");
			products.add(new Product(
					Long.parseLong(elements[0]),
					elements[1],
					elements[2],
					Integer.parseInt(elements[3]),
					new BigDecimal(elements[4]),
					Integer.parseInt(elements[5]),
					elements[6],
					convertToDate(elements[7]),
					convertToDate(elements[8])));
		}

		return products;
	}

	public Date convertToDate(String text) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(text);
		} catch (ParseException ex) {
			System.out.println("Error while parsing the date!");
		}

		return date;
	}
}
