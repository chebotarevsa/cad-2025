package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class SpringConfig {

	@Bean
	public CSVParser csvParser() {
		return new CSVParser();
	}

	@Bean
	public ResourceFileReader resourceFileReader() {
		return new ResourceFileReader();
	}

	@Bean
	public ConcreteProductProvider concreteProductProvider() {
		return new ConcreteProductProvider(resourceFileReader(), csvParser());
	}

	@Bean
	public ConsoleTableRenderer consoleTableRenderer() {
		return new ConsoleTableRenderer(concreteProductProvider());
	}
}
