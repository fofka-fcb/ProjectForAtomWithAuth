package ru.mypackage.exceptions;

import org.springframework.validation.BindingResult;

public class MessageDTOValidationException extends RuntimeException {

    private BindingResult bindingResult;

    public MessageDTOValidationException() {
    }

    public MessageDTOValidationException(String message) {
        super(message);
    }

    public MessageDTOValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

}
