package com.example.hospital.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // Validation errors (@Valid)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleValidation(
                        MethodArgumentNotValidException ex) {

                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .get(0)
                                .getDefaultMessage();

                ApiErrorResponse error = new ApiErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                message,
                                LocalDateTime.now());

                return ResponseEntity.badRequest().body(error);
        }

        // Constraint violations (e.g. @NotNull on params)
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
                        ConstraintViolationException ex) {

                ApiErrorResponse error = new ApiErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity.badRequest().body(error);
        }

        // Resource not found / business errors
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ApiErrorResponse> handleRuntime(RuntimeException ex) {

                ApiErrorResponse error = new ApiErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                ex.getMessage(),
                                LocalDateTime.now());

                return ResponseEntity.badRequest().body(error);
        }

        // Fallback (unexpected errors)
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {

                ex.printStackTrace(); // Added for debugging

                ApiErrorResponse error = new ApiErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Something went wrong",
                                LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
}