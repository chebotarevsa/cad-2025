package ru.bsuedu.cad.lab;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import ru.bsuedu.cad.lab.config.ConfigDB;
import ru.bsuedu.cad.lab.config.ConfigSecurity;
import ru.bsuedu.cad.lab.config.ConfigWeb;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

     @Override
    protected Class<?>[] getRootConfigClasses() {
            return new Class<?>[] { ConfigDB.class, ConfigSecurity.class};
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
