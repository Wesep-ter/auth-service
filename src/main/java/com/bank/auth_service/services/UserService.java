package com.bank.auth_service.services;



import com.bank.auth_service.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getById(Integer id);
    List<UserDto> getAll();
    void addUser(UserDto item);
    void update(Integer id,UserDto item);
    void delete(Integer id);

}
