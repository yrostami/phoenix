package com.phoenix.controller;

import org.springframework.validation.Errors;

public class ValidationException extends Exception {

	private ValidationError validationError;

	public ValidationException(Errors errors)
	{
		validationError = ValidationError.createFromBindingErrors(errors);
	}

	public ValidationError getValidationError() {
		return validationError;
	}
	
	
}
