package ru.bsuedu.cad.lab;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceFileReader implements Reader {
	private Provider provider;

	@Autowired
	public ResourceFileReader(Provider provider) {
		this.provider = provider;
	}

	@PostConstruct
	public void init() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		String formattedDateTime = now.format(formatter);
		System.out.println(String.format("Bean был полность проинициализирован: " + formattedDateTime));
	}

	@Override
	public String read() {
		String path = provider.getFileName();

		try {
			return Files.readString(Paths.get(getClass().getClassLoader().getResource(path).toURI()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}