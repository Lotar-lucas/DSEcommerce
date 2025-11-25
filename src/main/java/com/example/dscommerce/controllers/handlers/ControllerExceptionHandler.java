package com.example.dscommerce.controllers.handlers;

import com.example.dscommerce.dto.CustomError;
import com.example.dscommerce.dto.ValidationError;
import com.example.dscommerce.services.exceptions.DatabaseException;
import com.example.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

// Class for handling exceptions globally in controllers
@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<CustomError> database(DatabaseException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

    //Upcasting to ValidationError to add field-specific errors
    ValidationError err = new ValidationError(Instant.now(), status.value(), "Invalid data", request.getRequestURI());

    for (FieldError fieldError : e.getBindingResult().getFieldErrors()){
      err.addError(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return ResponseEntity.status(status).body(err);
  }
}
