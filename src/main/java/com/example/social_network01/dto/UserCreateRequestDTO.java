package com.example.social_network01.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateRequestDTO {

    private String login;
    private String role;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String appointment;
    private LocalDate birthday;
    private String phoneNumber;
    @NotBlank
    @Size(min = 8, message = "Пароль должен быть не короче 8 символов")
    private String password;

}