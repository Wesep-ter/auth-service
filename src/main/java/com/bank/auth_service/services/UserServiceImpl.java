package com.bank.auth_service.services;

import com.bank.auth_service.dto.UserDto;
import com.bank.auth_service.dto.mapper.UserMapper;
import com.bank.auth_service.entity.User;
import com.bank.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not fount by id: " + id));
        return UserMapper.toDTO(user) ;
    }

    @Override
    public List<UserDto> getAll() {
        return List.of();
    }

    @Override
    public void create(UserDto item) {

    }

    @Override
    public void update(Integer id, UserDto item) {

    }

    @Override
    public void delete(Integer id) {

    }
}
