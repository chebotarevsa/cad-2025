package ru.bsuedu.cad.lab;

import java.util.List;

public class ConcreteProductProvider implements ProductProvider {
	private Reader reader;
	private Parser parser;

	public ConcreteProductProvider(Reader reader, Parser parser) {
		this.reader = reader;
		this.parser = parser;
	}

	@Override
	public List<Product> getProducts() {
		return parser.parse(reader.read());
	}

}
