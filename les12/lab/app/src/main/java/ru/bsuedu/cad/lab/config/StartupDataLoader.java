package ru.bsuedu.cad.lab.config;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ru.bsuedu.cad.lab.service.CsvDataImportService;

@Component
public class StartupDataLoader {

    private final AtomicBoolean loaded = new AtomicBoolean(false);
    private final CsvDataImportService csvDataImportService;

    public StartupDataLoader(CsvDataImportService csvDataImportService) {
        this.csvDataImportService = csvDataImportService;
    }

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent event) {
        if (loaded.compareAndSet(false, true)) {
            csvDataImportService.importAll();
        }
    }
}
