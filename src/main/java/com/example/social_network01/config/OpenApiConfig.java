package com.example.social_network01.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Documentation",
                version = "1.0",
                description = "Documentation for API endpoints"
        )
)
public class OpenApiConfig {
    // Дополнительные настройки
}
