package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("application.properties")
public class ValueProvider implements Provider {

	@Value("${filename}")
	private String filename;

	@Override
	public String getFileName() {
		return filename;
	}

}