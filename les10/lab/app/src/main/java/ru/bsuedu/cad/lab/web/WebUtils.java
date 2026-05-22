package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletContext;
import org.springframework.context.ApplicationContext;

public final class WebUtils {
    private WebUtils() {
    }

    public static <T> T getBean(ServletContext servletContext, Class<T> type) {
        ApplicationContext context =
                (ApplicationContext) servletContext.getAttribute(ApplicationListener.SPRING_CONTEXT);
        return context.getBean(type);
    }

    public static String html(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
