package com.phoenix.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ValidationError {

	private final String errorMessage;
	
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> errors = new ArrayList<String>();

    public ValidationError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void addValidationError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }	
    

    public static ValidationError createFromBindingErrors(Errors errors) {
        ValidationError validationError = new ValidationError(errors.getErrorCount() + " خطای اعتبار سنجی :");
        for (ObjectError objectError : errors.getAllErrors()) {
        	validationError.addValidationError(objectError.getDefaultMessage());
        }
        return validationError;
    }
}
