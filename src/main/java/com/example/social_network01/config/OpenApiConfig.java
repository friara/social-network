package com.example.social_network01.config;//package com.example.social_network01.config;
//
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.security.OAuthFlow;
//import io.swagger.v3.oas.annotations.security.OAuthFlows;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;
//
//@OpenAPIDefinition(
//        info = @Info(
//                contact = @Contact(
//                        name = "Anna Frich",
//                        email = "frich.anna5@gmail.com",
//                        url = "https://github.com/friara/social-network"
//                ),
//                description = "OpenApi documentation for Spring Security",
//                title = "OpenApi specification",
//                version = "1.0"
//        )
//)
//
//public class OpenApiConfig {
//}

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
