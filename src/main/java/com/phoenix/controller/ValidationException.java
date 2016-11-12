package com.phoenix.controller;

import org.springframework.validation.Errors;

public class ValidationException extends Exception {

	private Error validationError;

	public ValidationException(Errors errors)
	{
		validationError = Error.createFromBindingErrors(errors);
	}
	
	public ValidationException(Error ve)
	{
		validationError = ve;
	}

	public Error getValidationError() {
		return validationError;
	}
	
	
}
