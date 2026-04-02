package com.bank.auth_service.services;
import com.bank.auth_service.dto.SignInResponse;
import com.bank.auth_service.dto.SignUpRequest;
import com.bank.auth_service.dto.UserDto;
import com.bank.auth_service.dto.mapper.UserMapper;
import com.bank.auth_service.entity.Role;
import com.bank.auth_service.entity.User;
import com.bank.auth_service.exception.UserAlreadyExistException;
import com.bank.auth_service.repositories.UserRepository;
import com.bank.auth_service.security.jwt.CustomUserDetails;
import com.bank.auth_service.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserDto signUp(SignUpRequest signUpRequest) {
        userRepository.findByUserLogin(signUpRequest.getUserLogin()).ifPresent(user -> {
                   throw new UserAlreadyExistException(user.getUserLogin() + " логин уже существует.");
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
       try {
           Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
           String token = jwtService.generateJwtToken((CustomUserDetails)authentication.getPrincipal());
           return SignInResponse.builder()
                   .message("Аутентификация прошла успешно.")
                   .timestamp(LocalDateTime.now())
                   .token(token)
                   .build();
       }catch (AuthenticationException ex){
           log.info("Authentication exception: " + ex.getMessage());
           return SignInResponse.builder()
                   .message("Неправильно введён логин или пароль.")
                   .timestamp(LocalDateTime.now())
                   .build();
       }
    }

}
