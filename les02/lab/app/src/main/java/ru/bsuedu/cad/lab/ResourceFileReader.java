package ru.bsuedu.cad.lab;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceFileReader implements Reader {

	@Override
	public String read() {
		String path = "product.csv";

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
