package com.macode101.exam.exception;

import com.macode101.exam.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        logger.warn("User not found - Request: {} {}, Error: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                Instant.now().toEpochMilli()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ex.getBindingResult().getGlobalErrors().forEach(error ->
            errors.add("Global: " + error.getDefaultMessage())
        );

        logger.warn("Validation failed - Request: {} {}, Errors: {}", request.getMethod(), request.getRequestURI(), errors);

        ErrorResponse errorResponse = new ErrorResponse(
                "Validation failed for the provided data",
                HttpStatus.BAD_REQUEST.value(),
                Instant.now().toEpochMilli(),
                errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.warn("Malformed JSON request - Request: {} {}, Error: {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        
        List<String> errors = new ArrayList<>();
        errors.add("Malformed JSON request body");
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid JSON format in request body",
                HttpStatus.BAD_REQUEST.value(),
                Instant.now().toEpochMilli(),
                errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        logger.warn("Invalid parameter type - Request: {} {}, Parameter: {}, Value: {}", 
                   request.getMethod(), request.getRequestURI(), ex.getName(), ex.getValue());
        
        List<String> errors = new ArrayList<>();
        errors.add(String.format("Invalid value '%s' for parameter '%s'. Expected type: %s", 
                                ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName()));
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid parameter type provided",
                HttpStatus.BAD_REQUEST.value(),
                Instant.now().toEpochMilli(),
                errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariableException(MissingPathVariableException ex, HttpServletRequest request) {
        logger.warn("Missing path variable - Request: {} {}, Variable: {}", request.getMethod(), request.getRequestURI(), ex.getVariableName());
        
        List<String> errors = new ArrayList<>();
        errors.add(String.format("Missing required path variable: %s", ex.getVariableName()));
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Missing required path parameter",
                HttpStatus.BAD_REQUEST.value(),
                Instant.now().toEpochMilli(),
                errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        logger.warn("Method not supported - Request: {} {}, Supported methods: {}", 
                   request.getMethod(), request.getRequestURI(), ex.getSupportedMethods());
        
        List<String> errors = new ArrayList<>();
        errors.add(String.format("HTTP method '%s' is not supported for this endpoint", ex.getMethod()));
        errors.add(String.format("Supported methods: %s", String.join(", ", ex.getSupportedMethods())));
        
        ErrorResponse errorResponse = new ErrorResponse(
                "HTTP method not supported",
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                Instant.now().toEpochMilli(),
                errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(jakarta.validation.ConstraintViolationException ex, HttpServletRequest request) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        logger.warn("Constraint violation - Request: {} {}, Errors: {}", request.getMethod(), request.getRequestURI(), errors);

        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid parameter provided",
                HttpStatus.BAD_REQUEST.value(),
                Instant.now().toEpochMilli(),
                errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error - Request: {} {}, Error: {}", request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Instant.now().toEpochMilli()
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
