package ru.bsuedu.cad.lab;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConcreteCategoryProvider implements DataProvider<Category> {
	private Reader reader;
	private Parser<Category> parser;

	@Autowired
	public ConcreteCategoryProvider(Reader reader, Parser<Category> parser) {
		this.reader = reader;
		this.parser = parser;
	}

	@Override
	public List<Category> getItems() {
		return parser.parse(reader.read("categoryFile"));
	}
}
