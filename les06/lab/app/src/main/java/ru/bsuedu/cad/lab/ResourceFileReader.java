package ru.bsuedu.cad.lab;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceFileReader implements Reader {
	private Provider provider;

	@Autowired
	public ResourceFileReader(Provider provider) {
		this.provider = provider;
	}

	@Override
	public String read(String fileKey) {
		String path = provider.getFileName().get(fileKey);

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
