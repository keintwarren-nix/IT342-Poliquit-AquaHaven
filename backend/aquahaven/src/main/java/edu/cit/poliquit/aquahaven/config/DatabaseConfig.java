package edu.cit.poliquit.aquahaven.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() {
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            String jdbcUrl = databaseUrl;
            if (jdbcUrl.startsWith("postgresql://")) {
                jdbcUrl = "jdbc:" + jdbcUrl;
            }
            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .build();
        }
        return DataSourceBuilder.create().build();
    }
}
