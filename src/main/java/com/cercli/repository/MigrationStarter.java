package com.cercli.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Db migration starter
 * Uses migrations from the `resources/migrations` folder
 */
@Service
@RequiredArgsConstructor
public class MigrationStarter implements ApplicationListener<ContextRefreshedEvent> {
    private final Migrator migrator;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try (var migrationsStream = Files.list(Path.of(this.getClass().getClassLoader()
                .getResource("migrations")
                .toURI()))) {
            migrator.migrate(migrationsStream.toList());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
