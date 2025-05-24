package com.example.social_network01;

import com.example.social_network01.model.User;
import com.example.social_network01.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan("com.example.social_network01.model")
@EnableScheduling
//@EnableAsync
public class SocialNetwork01Application {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // Принудительная установка UTC
		System.out.println("Часовой пояс JVM: " + ZoneId.systemDefault());
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
