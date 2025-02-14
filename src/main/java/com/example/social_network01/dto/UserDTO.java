package com.example.social_network01.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String login;
    private String role;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String appointment;
    private LocalDate birthday;
    private String avatarUrl;
}
