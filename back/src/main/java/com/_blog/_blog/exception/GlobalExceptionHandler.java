package com._blog._blog.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com._blog._blog.dto.ApiResponse;
import com._blog._blog.dto.ErrorItem;

import jakarta.validation.ConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle all uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, List.of(new ErrorItem(null, ex.getMessage())), null));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptionsMethodNot(Exception ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ApiResponse<>(false, List.of(new ErrorItem(null, ex.getMessage())), null));
    }

    // Handle validation errors (return list of ErrorItem)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorItem> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> new ErrorItem(err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, errors, null));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException ex) {
        ErrorItem errorItem = new ErrorItem(ex.getField(), ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, List.of(errorItem), null));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(
            ConstraintViolationException ex) {

        List<ErrorItem> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> new ErrorItem(
                        v.getPropertyPath().toString(),
                        v.getMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(false, errors, null));
    }

}
