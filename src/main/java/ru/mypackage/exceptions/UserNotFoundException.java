package ru.mypackage.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException{

    private final HttpStatus status;

    public UserNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

}
