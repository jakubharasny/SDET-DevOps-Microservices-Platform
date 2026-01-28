package com.example.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

import net.datafaker.Faker;

public class SeedUserRole {

    // Default connection settings so the seeder runs with no env vars.
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DB = "sdet_dev";
    private static final String DEFAULT_USER = "sdet";
    private static final String DEFAULT_PASSWORD = "sdetpass";
    private static final int DEFAULT_COUNT = 100;

    public static void main(String[] args) throws Exception {
        // Read connection/seeding options from environment with sensible defaults.
        String host = getenvOrDefault("DB_HOST", DEFAULT_HOST);
        String port = getenvOrDefault("DB_PORT", DEFAULT_PORT);
        String db = getenvOrDefault("DB_NAME", DEFAULT_DB);
        String user = getenvOrDefault("DB_USER", DEFAULT_USER);
        String password = getenvOrDefault("DB_PASSWORD", DEFAULT_PASSWORD);
        int count = Integer.parseInt(getenvOrDefault("SEED_COUNT", String.valueOf(DEFAULT_COUNT)));

        // JDBC connection string for local MySQL.
        String jdbcUrl = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true",
                host, port, db);

        // Faker generates realistic names, jobs, and sentences.
        Faker faker = new Faker(Locale.ENGLISH);
        // Random controls probabilities for enabled/updated/description fields.
        Random random = new Random();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusYears(3);
        int rolePoolSize = Math.max(5, Math.min(25, count / 4));
        String[] rolePool = new String[rolePoolSize];
        for (int i = 0; i < rolePoolSize; i++) {
            rolePool[i] = clamp(faker.job().title(), 100);
        }

        // Prepared statement for the UserRole table.
        String insertSql = """
                INSERT INTO UserRole
                    (Name, Description, IsEnabled, Created, CreatedBy, Updated, UpdatedBy)
                VALUES
                    (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
             PreparedStatement statement = connection.prepareStatement(insertSql)) {

            for (int i = 0; i < count; i++) {
                // Role name is picked from a smaller pool so duplicates exist.
                String name = rolePool[random.nextInt(rolePool.length)];
                // Most rows have a description, some are NULL.
                String description = random.nextDouble() < 0.8
                        ? clamp(faker.lorem().sentence(8), 200)
                        : null;

                // Created date is within the last 3 years.
                LocalDate created = randomDate(startDate, today, random);
                String createdBy = clamp(faker.name().fullName(), 200);

                // Some rows have an updated date/user, others remain NULL.
                LocalDate updated = null;
                String updatedBy = null;
                if (random.nextDouble() < 0.4) {
                    updated = randomDate(created, today, random);
                    updatedBy = clamp(faker.name().fullName(), 200);
                }

                // Most roles are enabled.
                boolean enabled = random.nextDouble() < 0.85;

                // Bind row values to the prepared statement.
                statement.setString(1, name);
                statement.setString(2, description);
                statement.setBoolean(3, enabled);
                statement.setObject(4, created);
                statement.setString(5, createdBy);
                if (updated == null) {
                    statement.setNull(6, Types.DATE);
                } else {
                    statement.setObject(6, updated);
                }
                if (updatedBy == null) {
                    statement.setNull(7, Types.VARCHAR);
                } else {
                    statement.setString(7, updatedBy);
                }

                // Queue the row for batch insert.
                statement.addBatch();
            }

            // Insert all rows in one batch for speed.
            statement.executeBatch();
        }
    }

    // Helper: read environment variable with a fallback.
    private static String getenvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    // Helper: enforce VARCHAR length limits.
    private static String clamp(String value, int maxLen) {
        return value.length() <= maxLen ? value : value.substring(0, maxLen);
    }

    // Helper: pick a random date between two bounds (inclusive).
    private static LocalDate randomDate(LocalDate start, LocalDate end, Random random) {
        if (!start.isBefore(end)) {
            return start;
        }
        int days = (int) (end.toEpochDay() - start.toEpochDay());
        return start.plusDays(random.nextInt(days + 1));
    }
}
