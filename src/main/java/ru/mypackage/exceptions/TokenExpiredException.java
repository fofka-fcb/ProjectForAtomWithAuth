package ru.mypackage.exceptions;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends RuntimeException{

    private final HttpStatus status;
    public TokenExpiredException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

}
