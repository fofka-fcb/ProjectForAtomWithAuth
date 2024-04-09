package ru.mypackage.exceptions;

import org.springframework.http.HttpStatus;

public class MessageNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public MessageNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }
}
