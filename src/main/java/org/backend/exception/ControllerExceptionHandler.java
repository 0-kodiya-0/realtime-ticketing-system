package org.backend.exception;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.backend.dto.responsebody.ApiResponse;
import org.backend.dto.responsebody.WriteResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = "org.example.opp_cw.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(violation -> {
            String fieldName = violation.getField();
            String errorMessage = violation.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, errors, request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationExceptions(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, errors, request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<ApiResponse> handleMongoDbExceptions(MongoWriteException ex, HttpServletRequest request) {
        if (ex.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
            WriteResponse errorResponse = new WriteResponse(ex.getError(), request);
            return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
        }
        ApiResponse apiResponse = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getError().getCategory().toString(), request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

//    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
//    public ResponseEntity<ApiResponse> handleIncorrectResultSizeExceptions(IncorrectResultSizeDataAccessException ex, HttpServletRequest request) {
//        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
//        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
//    }
}
