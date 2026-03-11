package com.bank.auth_service.security.jwt;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TokenData {
    private Long id;
    private List<String> roles;
    private String name;
    private String lastName;
}
