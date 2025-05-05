package com.example.social_network01.config.security.token;

import com.example.social_network01.model.User;
import com.example.social_network01.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import com.example.social_network01.exception.custom.UserNotFoundException;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // 1. Извлеките данные из токена
        String username = jwt.getClaim("sub");

        // 2. Загрузите пользователя из БД
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // 3. Создайте объект аутентификации
        return new UsernamePasswordAuthenticationToken(
                user,
                jwt,
                user.getAuthorities()
        );
    }
}
