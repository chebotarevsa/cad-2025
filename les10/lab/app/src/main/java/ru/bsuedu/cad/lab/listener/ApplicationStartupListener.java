package ru.bsuedu.cad.lab.listener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import ru.bsuedu.cad.lab.service.CsvDataImportService;

public class ApplicationStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext context =
                WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
        context.getBean(CsvDataImportService.class).importAll();
    }
}
