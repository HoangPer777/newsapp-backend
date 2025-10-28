package com.example.newsapp.common;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
    return ResponseEntity.badRequest().body(Map.of("error","validation", "detail", ex.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> handleRuntime(RuntimeException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","bad_request","detail", ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleOthers(Exception ex){
    return ResponseEntity.status(500).body(Map.of("error","internal_error"));
  }
}
