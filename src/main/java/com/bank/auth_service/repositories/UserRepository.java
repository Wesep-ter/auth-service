package com.bank.auth_service.repositories;

import com.bank.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUserId(Integer id);
    Optional<User> findByLogin(String login);
}
