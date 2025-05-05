package com.example.social_network01.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${avatar.storage-path}")
    private String avatarsPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Аватарки
        registry.addResourceHandler("/api/avatars/**")
                .addResourceLocations("file:" + avatarsPath + "/")
                .setCachePeriod(3600);

    }
}
