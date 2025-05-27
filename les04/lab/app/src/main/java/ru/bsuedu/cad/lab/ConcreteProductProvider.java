package ru.bsuedu.cad.lab;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConcreteProductProvider implements ProductProvider {
	private Reader reader;
	private Parser parser;

	@Autowired
	public ConcreteProductProvider(Reader reader, Parser parser) {
		this.reader = reader;
		this.parser = parser;
	}

	@Override
	public List<Product> getProducts() {
		return parser.parse(reader.read());
	}

}