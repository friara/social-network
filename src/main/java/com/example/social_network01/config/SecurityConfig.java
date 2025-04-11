package com.example.social_network01.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorize) -> authorize
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults())
//                .formLogin(withDefaults())
//        .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
//                .jwt(jwt -> jwt
//                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                )
//        )
//                .sessionManagement(sessionManagement -> sessionManagement
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//        return http.build();
//    }
//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter());
//        return jwtAuthenticationConverter;
//    }
//
//
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                                .anyRequest().permitAll()
//                        //.requestMatchers("/h2-console/**").permitAll() // Разрешить доступ к H2 консоли
//                        //.anyRequest().authenticated()
//                )
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers("/h2-console/**") // Отключить CSRF для H2 консоли
//                )
//                .headers(headers -> headers
//                        .frameOptions(frameOptions -> frameOptions
//                                .sameOrigin() // Разрешить встроенные фреймы для H2 консоли
//                        )
//                );
//        return http.build();
//    }
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(authorize -> authorize
                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.jwkSetUri("https://localhost:9000/.well-known/jwks.json"))
            );
//            .oauth2Login(oauth2Login -> oauth2Login
//                    .loginPage("/login") // Укажите путь к вашему серверу авторизации
////                    .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
////                            .authorizationRequestRepository(cookieAuthorizationRequestRepository())
////                            .authorizationRequestResolver(customAuthorizationRequestResolver())
////                    )
//                    .redirectionEndpoint(redirectionEndpoint -> redirectionEndpoint
//                            .baseUri("http://localhost:9000/login") // Базовый URI сервера авторизации
//                    )
//                    );
    return http.build();
}

//    .oauth2ResourceServer(oauth2ResourceServer ->
//            oauth2ResourceServer
//                    .jwt(jwt ->
//            jwt
//                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
//            )
//            );
//}
//
//@Bean
//public JwtAuthenticationConverter jwtAuthenticationConverter() {
//    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//    grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // Укажите имя claim, содержащего роли
//    grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Префикс для ролей
//
//    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//
//    return jwtAuthenticationConverter;
//}
//}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }





}