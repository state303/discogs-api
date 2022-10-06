package io.dsub.discogs.api.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;

@Slf4j
@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class, ValidationException.class})
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.badRequest().body(e.getLocalizedMessage());
    }
}
