package com.cercli.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import static com.cercli.platform.Checks.checkThat;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readString;
import static java.util.Comparator.comparing;

/**
 * Db migrator
 * Applies provided sql migration scripts to the db
 */
@Service
@RequiredArgsConstructor
public class Migrator {
    private final Pattern FILE_NAME_PATTERN = Pattern.compile("V\\d{3}__[\\w_]+.sql");

    private final JdbcTemplate jdbcTemplate;

    public void migrate(List<Path> migrations) {
        var migrationFiles = migrations.stream()
                .sorted(comparing(it -> it.getFileName().toString()))
                .toList();
        migrationFiles.forEach(this::validate);
        migrationFiles.forEach(this::migrate);
    }

    private void validate(Path file) {
        var fileName = file.getFileName().toString();
        checkThat(FILE_NAME_PATTERN.matcher(fileName).find(),
                "Migration file %s must have the following format: V001__migration_name.sql",
                fileName);
    }

    private void migrate(Path file) {
        try {
            var script = readString(file, UTF_8);
            jdbcTemplate.execute(script);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
