package com.bank.auth_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\-]+$")
    private String name;

    @NotBlank
    @Size(min = 2, max = 50)
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\-]+$")
    private String lastName;

    @Size(max = 60)
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z\\-]*$")
    private String middleName;

    @NotNull
    @Past
    private LocalDate birthday;

    @NotBlank
    @Size(min = 5, max = 254)
    private String userLogin;

    @NotBlank
    @Size(min = 8, max = 128)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$")
    private String userPassword;

    @NotBlank
    @Pattern(regexp = "^\\+?[1-9]\\d{10,14}$")
    private String phoneNumber;
}
