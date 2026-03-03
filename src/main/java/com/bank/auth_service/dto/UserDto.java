package com.bank.auth_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("roles")
    private String roles;
    @JsonProperty("user_login")
    private String userLogin;
}
