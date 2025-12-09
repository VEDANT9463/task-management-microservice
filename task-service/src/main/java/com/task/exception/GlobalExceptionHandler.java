package com.task.exception;


import com.task.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex, HttpServletRequest req) {
        return new ErrorResponse(ex.getMessage(), req.getRequestURI(), LocalDateTime.now());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTaskNotFound(TaskNotFoundException ex, HttpServletRequest req) {
        return new ErrorResponse(ex.getMessage(), req.getRequestURI(), LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
        return new ErrorResponse(ex.getMessage(), req.getRequestURI(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex, HttpServletRequest req) {
        return new ErrorResponse("Internal Server Error", req.getRequestURI(), LocalDateTime.now());
    }
}

