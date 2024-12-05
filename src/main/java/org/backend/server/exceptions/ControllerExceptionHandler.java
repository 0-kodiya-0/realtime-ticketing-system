package org.backend.server.exceptions;

import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.backend.server.dto.ApiResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.AccountException;
import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = {
        "org.backend.server.microservices.ticketpool.controller",
        "org.backend.server.microservices.authorization.controller"
})
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()).createResponse();
    }

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(violation -> {
            String fieldName = violation.getField();
            String errorMessage = violation.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ApiResponse(HttpStatus.BAD_REQUEST, String.valueOf(errors)).createResponse();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ApiResponse(HttpStatus.BAD_REQUEST, String.valueOf(errors)).createResponse();
    }
}
