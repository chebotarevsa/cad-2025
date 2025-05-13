package ru.bsuedu.cap.hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.bsuedu.cap.hello.impl.MessageProviderImpl;
import ru.bsuedu.cap.hello.impl.MessageRendererImpl;

@Configuration
public class MessageConfiguration {

    @Bean
    MessageProvider  provider() {
        return new MessageProviderImpl();
    } 
    
    @Bean
    MessageRenderer renderer(MessageProvider provider) {
        MessageRenderer renderer = new MessageRendererImpl();
        renderer.setMessageProvider(provider);
        return renderer;
    } 
}
