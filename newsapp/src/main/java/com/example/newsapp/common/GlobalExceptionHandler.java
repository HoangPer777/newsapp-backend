package com.example.newsapp.common;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
    return ResponseEntity.badRequest().body(Map.of("error", "validation", "detail", ex.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> handleRuntime(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", "bad_request", "detail", ex.getMessage()));
  }

  @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
  public ResponseEntity<?> handleResponseStatus(org.springframework.web.server.ResponseStatusException ex) {
    return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", "error", "detail", ex.getReason()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleOthers(Exception ex) {
    ex.printStackTrace(); // Log the error for debugging
    return ResponseEntity.status(500).body(Map.of("error", "internal_error", "detail", ex.getMessage()));
  }
}
