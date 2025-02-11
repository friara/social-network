package com.example.social_network01.controller;

import com.example.social_network01.dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Hello, Public!";
    }

    @GetMapping("/private/hello")
    public String privateHello() {
        return "Hello, Private!";
    }
}

