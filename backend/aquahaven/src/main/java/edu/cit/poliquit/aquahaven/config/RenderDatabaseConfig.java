package edu.cit.poliquit.aquahaven.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class RenderDatabaseConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = environment.getProperty("DATABASE_URL");
        
        if (databaseUrl != null && databaseUrl.startsWith("postgresql://")) {
            Map<String, Object> properties = new HashMap<>();
            String jdbcUrl = "jdbc:" + databaseUrl;
            properties.put("spring.datasource.url", jdbcUrl);
            
            MapPropertySource propertySource = new MapPropertySource("render-db-config", properties);
            environment.getPropertySources().addFirst(propertySource);
        }
    }
}
