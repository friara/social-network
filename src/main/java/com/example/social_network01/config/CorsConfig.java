package com.example.social_network01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // Домен Flutter-клиента
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Для работы с куки/токенами
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*");
                // Разрешаем запросы для Swagger UI
                registry.addMapping("/swagger-ui/**")
                        .allowedOrigins("http://localhost:8080")  // URL Spring Boot-приложения
                        .allowedMethods("GET")
                        .allowedHeaders("*");

                // Разрешаем доступ к OpenAPI документации
                registry.addMapping("/v3/api-docs/**")
                        .allowedOrigins("*")  // Доступ из любого источника
                        .allowedMethods("GET");
            }
        };
    }
}
