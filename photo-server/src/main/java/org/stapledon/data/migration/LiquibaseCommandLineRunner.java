package org.stapledon.data.migration;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiquibaseCommandLineRunner implements CommandLineRunner {

    private ApplicationContext ctx;
    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/postgres}")
    String datasource;

    @Value("${spring.datasource.username:postgres}")
    String username;

    @Value("${spring.datasource.password:postgres}")
    String password;

    @Value("${spring.liquibase.contexts:default}")
    String context;

    @Override
    public void run(String... args) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--db-setup")) {
                runLiquibaseMigration(datasource, username, password, "db/changelog/db.changelog-master.xml");
                System.exit(SpringApplication.exit(ctx, () -> 0));
            }
        }
    }

    /**
     * Run a Liquibase migration using the given JDBC URL, username, password, and changelog file.
     *
     * @param url       JDBC URL
     * @param user      username for the database
     * @param password  password for the database
     * @param changelog path to the changelog file
     */
    public void runLiquibaseMigration(String url, String user, String password, String changelog) {
        try {
            Database database;
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            }
            // Create a Liquibase object with the changelog file
            try (Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database)) {
                liquibase.update(context);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
