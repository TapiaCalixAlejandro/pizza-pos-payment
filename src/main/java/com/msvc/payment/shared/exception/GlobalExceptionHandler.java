package com.msvc.payment.shared.exception;

import com.msvc.payment.shared.responses.ApiErrorResponse;
import com.msvc.payment.shared.responses.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ResponseFactory responseFactory;

    public GlobalExceptionHandler(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleNotValidationException(MethodArgumentNotValidException exception) {
        List<String> details = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return ResponseEntity
                .badRequest()
                .body(
                        responseFactory.error(
                                HttpStatus.BAD_REQUEST,
                                "Request validation failed.",
                                details
                        )
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadableException(HttpMessageNotReadableException exception) {

        return ResponseEntity
                .badRequest()
                .body(
                        responseFactory.error(
                                HttpStatus.BAD_REQUEST,
                                "Malformed JSON request or invalid data types.",
                                List.of("Please check the request body format.")
                        )
                );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDatabaseError(DataIntegrityViolationException exception) {
        LOGGER.warn(
                "Database constraint violation.",
                exception
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        responseFactory.error(
                                HttpStatus.CONFLICT,
                                "The request could not be completed due to a data conflict.",
                                List.of("Duplicate entry or constraint violation.")
                        )
                );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(ResourceNotFoundException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseFactory.error(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(responseFactory.error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleConflictException(ConflictException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(responseFactory.error(HttpStatus.CONFLICT, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception) {
        LOGGER.error(
                "Unexpected error while processing request.",
                exception
        );

        return ResponseEntity
                .internalServerError()
                .body(
                        responseFactory.error(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred. Please contact support.",
                                List.of()
                        )
                );
    }

    @ExceptionHandler(OrderServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleOrderServiceException(OrderServiceException exception) {

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(
                        responseFactory.error(
                                HttpStatus.SERVICE_UNAVAILABLE,
                                exception.getMessage()
                        )
                );
    }
}
