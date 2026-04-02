package com.bank.auth_service.handler;

import com.bank.auth_service.exception.UserAlreadyExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(UserAlreadyExistException.class)
   public ResponseEntity<?> handleUserNotFoundEx(UserAlreadyExistException ex){
       return ResponseEntity.badRequest().body(ex.getMessage());
   }
}
