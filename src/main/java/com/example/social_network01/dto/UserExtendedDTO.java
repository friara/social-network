package com.example.social_network01.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


public class UserExtendedDTO extends UserDTO {

    @Getter
    @Setter
    @NotBlank
    @Size(min = 8, message = "Пароль должен быть не короче 8 символов")
    private String password;

}