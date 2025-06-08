package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				SpringConfig.class);

		ConsoleTableRenderer r = context.getBean("consoleTableRenderer", ConsoleTableRenderer.class);
		r.render();
		context.close();
	}
}