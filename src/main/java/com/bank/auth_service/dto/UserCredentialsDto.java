package com.bank.auth_service.dto;

import lombok.Data;

@Data
public class UserCredentialsDto {
    private String username;
    private String password;
}
