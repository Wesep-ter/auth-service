package com.bank.auth_service.dto;

import com.bank.auth_service.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("roles")
    private Set <Role> roles;
    @JsonProperty("user_login")
    private String userLogin;
}
