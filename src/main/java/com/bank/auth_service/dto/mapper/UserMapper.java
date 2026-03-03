package com.bank.auth_service.dto.mapper;


import com.bank.auth_service.dto.UserDto;
import com.bank.auth_service.entity.User;

public class UserMapper {

    public static UserDto toDTO(User user){
        return UserDto.builder()
                .id(user.getId())
                .roles(user.getRoles())
                .userLogin(user.getUserLogin())
                .build();
    }
}
