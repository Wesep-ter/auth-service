package com.bank.auth_service.services;

import com.bank.auth_service.dto.SignUpRequest;
import com.bank.auth_service.dto.UserDto;
import com.bank.auth_service.entity.Role;
import com.bank.auth_service.entity.User;
import com.bank.auth_service.exception.UserAlreadyExistException;
import com.bank.auth_service.repositories.UserRepository;
import com.bank.auth_service.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;


    private SignUpRequest signUpRequest;
    private User user;


    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
                .userLogin("testuser")
                .userPassword("password123")
                .name("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .birthday(LocalDate.of(1999, 1, 1))
                .phoneNumber("+79991234567")
                .build();

        user = User.builder()
                .id(1L)
                .userLogin("testuser")
                .userPassword("encodedPassword")
                .name("Иван")
                .lastName("Иванов")
                .middleName("Иванович")
                .birthday(LocalDate.of(1999, 1, 1))
                .phoneNumber("+79991234567")
                .roles(Set.of(Role.ROLE_CLIENT))
                .build();
    }
    @Test
    void signUp_Success() {
        when(userRepository.findByUserLogin("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = authService.signUp(signUpRequest);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUserLogin()).isEqualTo("testuser");
        assertThat(result.getRoles()).contains(Role.ROLE_CLIENT);

        verify(userRepository, times(1)).findByUserLogin("testuser");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void signUp_UserAlreadyExists_ThrowsException() {
        when(userRepository.findByUserLogin("testuser")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.signUp(signUpRequest))
                .isInstanceOf(UserAlreadyExistException.class)
                .hasMessageContaining("логин уже существует");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void signUp_PasswordIsEncoded() {

        when(userRepository.findByUserLogin("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        authService.signUp(signUpRequest);
        verify(passwordEncoder).encode("password123");
    }
}
