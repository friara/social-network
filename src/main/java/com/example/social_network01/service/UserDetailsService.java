package com.example.social_network01.service;

import com.example.social_network01.model.User;
import com.example.social_network01.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.*;

//@Bean
//public UserDetailsService userDetailsService(UserRepository userRepo) {
//    return username -> {
//        User user = userRepo.findByLogin(username);
//        if (user != null) return user;
//        throw new UsernameNotFoundException("User ‘" + username + "’ not found");
//    };
//}
