package ru.bsuedu.cap.hello;

import java.util.Properties;

public class MessageSupportFactory {
    private static MessageSupportFactory instance;

    private Properties properties;
    private MessageRenderer renderer;
    private MessageProvider provider;

    private MessageSupportFactory() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/config.properties"));
            String rendererClass = properties.getProperty("renderer.class");
            String providerClass = properties.getProperty("provider.class");
            System.out.println(rendererClass + " " + providerClass);
            renderer = (MessageRenderer) Class.forName(rendererClass).
            getDeclaredConstructor().newInstance();
            provider = (MessageProvider) Class.forName(providerClass).
            getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        instance = new MessageSupportFactory();
    }

    public static MessageSupportFactory getInstance() {
        return instance;
    }

    MessageRenderer getMessageRenderer(){
        return renderer;
    }

    MessageProvider getMessageProvider(){
        return provider;
    }
}
