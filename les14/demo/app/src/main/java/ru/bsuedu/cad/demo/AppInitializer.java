package ru.bsuedu.cad.demo;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import ru.bsuedu.cad.demo.config.ConfigDB;
import ru.bsuedu.cad.demo.config.ConfigSecurity;
import ru.bsuedu.cad.demo.config.ConfigWeb;


public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
            return new Class<?>[] {  ConfigDB.class, ConfigSecurity.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { ConfigWeb.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
}
