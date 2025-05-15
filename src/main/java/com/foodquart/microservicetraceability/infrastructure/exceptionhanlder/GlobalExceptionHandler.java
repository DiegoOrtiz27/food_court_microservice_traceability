package com.foodquart.microservicetraceability.infrastructure.exceptionhanlder;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foodquart.microservicetraceability.domain.exception.DomainException;
import com.foodquart.microservicetraceability.domain.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.foodquart.microservicetraceability.domain.util.HttpMessages.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String DOMAIN_ERROR_CODE = "DOMAIN_ERROR";
    public static final String UNAUTHORIZED_CODE = "UNAUTHORIZED";
    public static final String VALIDATION_FAILED_CODE = "VALIDATION_FAILED";
    public static final String INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR";
    public static final String UNKNOWN_FIELD_CODE = "UNKNOWN_FIELD";

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        return buildErrorResponse(DOMAIN_ERROR_CODE, ex.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return buildErrorResponse(UNAUTHORIZED_CODE, ex.getMessage(), UNAUTHORIZED);
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<ErrorResponse> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex) {
        String message = String.format(UNKNOWN_FIELD,
                ex.getPropertyName(), ex.getKnownPropertyIds());
        return buildErrorResponse(UNKNOWN_FIELD_CODE, message, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ValidationError> errors = fieldErrors.stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()))
                .toList();

        ValidationErrorResponse response = new ValidationErrorResponse(
                VALIDATION_FAILED_CODE,
                VALIDATION_FAILED,
                errors);

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error(UNEXPECTED_ERROR, ex);
        return buildErrorResponse(INTERNAL_SERVER_ERROR_CODE,
                UNEXPECTED_ERROR, INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String code, String message, HttpStatus status) {
        return new ResponseEntity<>(new ErrorResponse(code, message), status);
    }
}
