package ru.bsuedu.cap.component.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ru.bsuedu.cap.component.Provider;
import ru.bsuedu.cap.component.Renderer;

@Component("renderer")
public class RendererImpl implements Renderer {

    @Autowired
    //@Qualifier("providerSpEL")
    @Qualifier("providerResource")
    private  Provider provider;


    @Override
    public void render() {
        System.out.println(provider.getMessage());
    }

}
