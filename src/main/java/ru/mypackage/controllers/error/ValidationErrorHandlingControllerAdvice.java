package ru.mypackage.controllers.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mypackage.dto.error.MessageErrorResponse;
import ru.mypackage.dto.error.TopicErrorResponse;
import ru.mypackage.dto.error.UserErrorResponse;
import ru.mypackage.exceptions.AuthValidationException;
import ru.mypackage.exceptions.MessageDTOValidationException;
import ru.mypackage.exceptions.TopicDTOValidationException;

import java.util.List;

@RestControllerAdvice
public class ValidationErrorHandlingControllerAdvice {

    @ExceptionHandler(AuthValidationException.class)
    public ResponseEntity<UserErrorResponse> handleRegistrationValidateException(
            AuthValidationException e
    ) {
        StringBuilder exceptionMessage = new StringBuilder();

        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        for (FieldError error : errors) {
            exceptionMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        UserErrorResponse response = new UserErrorResponse(
                exceptionMessage.toString(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TopicDTOValidationException.class)
    public ResponseEntity<TopicErrorResponse> handleTopicDTOValidationException(
            TopicDTOValidationException e
    ) {
        StringBuilder exceptionMessage = new StringBuilder();

        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        for (FieldError error : errors) {
            exceptionMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        TopicErrorResponse response = new TopicErrorResponse(
                exceptionMessage.toString(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageDTOValidationException.class)
    public ResponseEntity<MessageErrorResponse> handleMessageDTOValidationException(
            MessageDTOValidationException e
    ) {
        StringBuilder exceptionMessage = new StringBuilder();

        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        for (FieldError error : errors) {
            exceptionMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        MessageErrorResponse response = new MessageErrorResponse(
                exceptionMessage.toString(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
