package com.bank.auth_service.controller;


import com.bank.auth_service.dto.SignInResponse;
import com.bank.auth_service.dto.SignUpRequest;
import com.bank.auth_service.dto.UserDto;
import com.bank.auth_service.dto.UserCredentials;
import com.bank.auth_service.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        UserDto body = authService.signUp(signUpRequest);
        return ResponseEntity.ok(body);
    }

    @PutMapping("/signIn")
    public ResponseEntity<SignInResponse> signIn(@RequestBody UserCredentials userCredentials){
        SignInResponse body = authService.signIn(userCredentials.getLogin(), userCredentials.getPassword());
        if(body.getToken()==null){
            return ResponseEntity.badRequest().body(body);
        }
        return ResponseEntity.ok(body);
    }
}
