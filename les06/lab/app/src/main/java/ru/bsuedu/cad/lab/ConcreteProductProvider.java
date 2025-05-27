package ru.bsuedu.cad.lab;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConcreteProductProvider implements DataProvider<Product> {
	private Reader reader;
	private Parser<Product> parser;

	@Autowired
	public ConcreteProductProvider(Reader reader, Parser<Product> parser) {
		this.reader = reader;
		this.parser = parser;
	}

	@Override
	public List<Product> getItems() {
		return parser.parse(reader.read("productFile"));
	}
}