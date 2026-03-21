package com.bank.auth_service.dto;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInResponse {
    String token;
    String message;
    LocalDateTime timestamp;
}
