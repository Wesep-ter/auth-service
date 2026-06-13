package com.bank.auth_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "roles")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "user_password")
    private String userPassword;



}
