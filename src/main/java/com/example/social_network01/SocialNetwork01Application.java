package com.example.social_network01;

import com.example.social_network01.model.User;
import com.example.social_network01.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SpringBootApplication
@EntityScan("com.example.social_network01.model")
public class SocialNetwork01Application {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetwork01Application.class, args);
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepo) {
		return username -> {
			User user = userRepo.findByLogin(username)
					.orElseThrow(() -> new UsernameNotFoundException(username));
			return user;
		};
	}
}
