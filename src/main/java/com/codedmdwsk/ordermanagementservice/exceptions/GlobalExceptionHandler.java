package com.codedmdwsk.ordermanagementservice.exceptions;


import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Resource Not Found");
        pd.setType(URI.create("https://example.com/problems/not-found"));
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }

    @ExceptionHandler(DuplicateCustomerException.class)
    public ProblemDetail handleDuplicate(DuplicateCustomerException ex, HttpServletRequest request) {
        log.warn("Duplicate customer: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Duplicate Customer");
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Bad request: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Bad Request");
        pd.setType(URI.create("https://example.com/problems/bad-request"));
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .distinct()
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation error");

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
        pd.setTitle("Validation Error");
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAny(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error"
        );
        pd.setTitle("Internal Server Error");
        pd.setType(URI.create("https://example.com/problems/internal"));
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        log.warn("Conflict: {}", ex.getMessage());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Conflict");
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn("Malformed JSON at {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed request body (invalid JSON or field format)"
        );
        pd.setTitle("Bad Request");
        pd.setType(URI.create("https://example.com/problems/bad-request"));
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }


    @ExceptionHandler(StreamReadException.class)
    public ProblemDetail handleStreamRead(StreamReadException ex, HttpServletRequest request) {
        log.warn("Bad JSON file at {}: {}", request.getRequestURI(), ex.getOriginalMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON file"
        );
        pd.setTitle("Bad Request");
        pd.setType(URI.create("https://example.com/problems/bad-request"));
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }


    @ExceptionHandler(JsonProcessingException.class)
    public ProblemDetail handleJsonProcessing(JsonProcessingException ex, HttpServletRequest request) {
        log.warn("JSON processing error at {}: {}", request.getRequestURI(), ex.getOriginalMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON file"
        );
        pd.setTitle("Bad Request");
        pd.setType(URI.create("https://example.com/problems/bad-request"));
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }

    @ExceptionHandler(JacksonException.class)
    public ProblemDetail handleJackson(JacksonException ex, HttpServletRequest request) {
        log.warn("Bad JSON in {}: {}", request.getRequestURI(), ex.getOriginalMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON file"
        );
        pd.setTitle("Bad Request");
        pd.setType(URI.create("https://example.com/problems/bad-request"));
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }


}
