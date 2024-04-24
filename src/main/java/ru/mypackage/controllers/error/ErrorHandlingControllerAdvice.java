package ru.mypackage.controllers.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mypackage.dto.error.MessageErrorResponse;
import ru.mypackage.dto.error.TokenErrorResponse;
import ru.mypackage.dto.error.TopicErrorResponse;
import ru.mypackage.dto.error.UserErrorResponse;
import ru.mypackage.exceptions.*;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleUsernameNotFoundException(
            UsernameNotFoundException e
    ) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<TokenErrorResponse> handleTokenExpiredException(
            TokenExpiredException e
    ) {
        TokenErrorResponse response = new TokenErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<MessageErrorResponse> handleMessageNotFoundException(
            MessageNotFoundException e
    ) {
        MessageErrorResponse response = new MessageErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<TopicErrorResponse> handleTopicNotFoundException(
            TopicNotFoundException e
    ) {
        TopicErrorResponse response = new TopicErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<UserErrorResponse> handleForbiddenException(
            ForbiddenException e
    ) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(
            UserNotFoundException e
    ) {
        UserErrorResponse response = new UserErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, e.getHttpStatus());
    }

}
