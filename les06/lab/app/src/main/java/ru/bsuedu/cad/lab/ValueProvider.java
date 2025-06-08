package ru.bsuedu.cad.lab;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("application.properties")
public class ValueProvider implements Provider {

	@Value("${product.filename}")
	private String product_filename;

	@Value("${category.filename}")
	private String category_filename;

	@Override
	public Map<String, String> getFileName() {
		return Map.of(
				"productFile", product_filename,
				"categoryFile", category_filename);
	};
}