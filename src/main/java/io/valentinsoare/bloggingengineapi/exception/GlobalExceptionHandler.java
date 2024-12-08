package io.valentinsoare.bloggingengineapi.exception;

import io.valentinsoare.bloggingengineapi.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            ResourceNotFoundException.class,
            NoElementsException.class
    })
    public ResponseEntity<ErrorResponse> handleExceptions(RuntimeException ex, WebRequest webRequest) {
        ErrorResponse err = ErrorResponse.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .details(webRequest.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceViolationException.class)
    public ResponseEntity<ErrorResponse> handleResourceViolationException(ResourceViolationException ex, WebRequest webRequest) {
        ErrorResponse err = ErrorResponse.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .details(webRequest.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest webRequest) {
        ErrorResponse err = ErrorResponse.builder()
                .message(String.format("%s", ex.getMessage()))
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .details(webRequest.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldNameError = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldNameError, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            AuthorizationDeniedException.class,
    })
    public ResponseEntity<ErrorResponse> handleAuthExceptions(RuntimeException ex, WebRequest webRequest) {
        ErrorResponse err = ErrorResponse.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .details(webRequest.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BloggingEngineException.class)
    public ResponseEntity<ErrorResponse> handleNewsOutletAPIException(RuntimeException ex, WebRequest webRequest) {
        ErrorResponse err = ErrorResponse.builder()
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .details(webRequest.getDescription(false))
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
}
