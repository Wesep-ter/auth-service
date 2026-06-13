package com.bank.auth_service.services;

import com.bank.auth_service.dto.SignInResponse;
import com.bank.auth_service.dto.SignUpRequest;
import com.bank.auth_service.dto.UserDto;

public interface AuthService {
    UserDto signUp(SignUpRequest signUpRequest);
    SignInResponse signIn(String login, String password);
}

