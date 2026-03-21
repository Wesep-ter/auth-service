package com.bank.auth_service.services;
import com.bank.auth_service.dto.SignInResponse;
import com.bank.auth_service.dto.SignUpRequest;
import com.bank.auth_service.dto.UserDto;
import com.bank.auth_service.dto.mapper.UserMapper;
import com.bank.auth_service.entity.Role;
import com.bank.auth_service.entity.User;
import com.bank.auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserDto signUp(SignUpRequest signUpRequest) {
        userRepository.findByUserLogin(signUpRequest.getUserLogin()).ifPresent(user -> {
                   throw new RuntimeException(user.getUserLogin() + " логин уже существует.");
                });
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setLastName(signUpRequest.getLastName());
        user.setMiddleName(signUpRequest.getMiddleName());
        user.setBirthday(signUpRequest.getBirthday());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setUserLogin(signUpRequest.getUserLogin());
        user.setUserPassword(passwordEncoder.encode(signUpRequest.getUserPassword()));
        user.setRoles(Set.of(Role.ROLE_CLIENT));
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public SignInResponse signIn(String login, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        //todo: Дописать метод
        return null;
    }

}
