
package com.evoting.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException exception) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 400);
        response.put("error", "Validation failed");

        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        fieldErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        response.put("errors", fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(
            InvalidCredentialsException exception) {

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmail(
            DuplicateEmailException exception) {

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ElectionStateException.class)
    public ResponseEntity<Map<String, String>> handleElectionState(
            ElectionStateException exception) {

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(DuplicateVoteException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateVote(
            DuplicateVoteException exception) {

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(
            IllegalStateException exception) {

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(
            RuntimeException exception) {

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
