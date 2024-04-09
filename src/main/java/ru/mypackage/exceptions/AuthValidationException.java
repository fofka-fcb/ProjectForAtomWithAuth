package ru.mypackage.exceptions;

import org.springframework.validation.BindingResult;

public class AuthValidationException extends RuntimeException{

    private BindingResult bindingResult;

    public AuthValidationException() {
    }

    public AuthValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

}
