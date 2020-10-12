package com.mirowidgets.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class WidgetNotFoundException extends ResponseStatusException {

    public WidgetNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, String.format("Widget not found id = %s", id.toString()));
    }
}
