package com.phoenix.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Error {

	private final String message;
	
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<String>();

    public Error(String errorMessage) {
        this.message = errorMessage;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getMessage() {
        return message;
    }	
    

    public static Error createFromBindingErrors(Errors errors) {
        Error validationError = new Error(errors.getErrorCount() + " خطای اعتبار سنجی :");
        for (ObjectError objectError : errors.getAllErrors()) {
        	validationError.addError(objectError.getDefaultMessage());
        }
        return validationError;
    }
}
