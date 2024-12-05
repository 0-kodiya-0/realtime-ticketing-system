package org.backend.server.microservices.authorization.exception;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.backend.server.dto.ApiResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.AccountException;
import java.util.Map;

@Order(2)
@ControllerAdvice(basePackages = {
        "org.backend.server.microservices.authorization.controller"
})
public class AuthServiceExceptionHandler {
    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ApiResponse> handleAccountException(AccountException ex) {
        return new ApiResponse(HttpStatus.UNAUTHORIZED, ex.getMessage()).createResponse();
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException ex) {
        return new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()).createResponse();
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> handleJwtException(JwtException ex) {
        return new ApiResponse(HttpStatus.UNAUTHORIZED, ex.getMessage()).createResponse();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ApiResponse(HttpStatus.NOT_FOUND, ex.getMessage()).createResponse();
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ApiResponse> handleEntityExistsException(EntityExistsException ex) {
        return new ApiResponse(HttpStatus.CONFLICT, ex.getMessage()).createResponse();
    }

    @ExceptionHandler(CompromisedPasswordException.class)
    public ResponseEntity<ApiResponse> handleCompromisedPasswordException(CompromisedPasswordException ex) {
        return new ApiResponse(HttpStatus.UNAUTHORIZED, Map.of("credentials.password", "double check the format")).createResponse();
    }
}
