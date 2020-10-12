package com.mirowidgets.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class WidgetExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<Object> statusCode(final ResponseStatusException ex) {
        LOGGER.debug("ERROR_INFO {} {}", ex.getStatus(), ex.getReason());

        return new ResponseEntity<>(new ErrorResponse(ex.getStatus().value(), ex.getReason()), ex.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Object> internalError(final Exception ex) {
        LOGGER.debug("INTERNAL ERROR", ex.getMessage());

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}