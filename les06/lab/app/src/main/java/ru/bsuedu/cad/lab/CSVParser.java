package ru.bsuedu.cad.lab;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class CSVParser<T> implements Parser<T> {

	@Override
	public List<T> parse(String text) {
		List<T> parsedObjects = new ArrayList<>();

		String[] rows = text.split("\n");

		for (int i = 1; i < rows.length; i++) {
			String row = rows[i].trim();
			if (!row.isEmpty()) {
				String[] elements = row.split(",");
				parsedObjects.add(parseRow(elements));
			}
		}

		return parsedObjects;
	}

	public abstract T parseRow(String[] elements);

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