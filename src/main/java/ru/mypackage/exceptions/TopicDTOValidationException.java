package ru.mypackage.exceptions;

import org.springframework.validation.BindingResult;

public class TopicDTOValidationException extends RuntimeException {

    private BindingResult bindingResult;

    public TopicDTOValidationException() {
    }

    public TopicDTOValidationException(String message) {
        super(message);
    }

    public TopicDTOValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

}
