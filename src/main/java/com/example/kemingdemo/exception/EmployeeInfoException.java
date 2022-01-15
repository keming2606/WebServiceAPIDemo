package com.example.kemingdemo.exception;

import java.util.List;

import com.example.kemingdemo.utils.ApplicationConstants;

public class EmployeeInfoException extends RuntimeException {

	public EmployeeInfoException(String exception) {
		super("{\"" + ApplicationConstants.JSON_MESSAGE + "\": \"" + exception + "\"}");
	}

	public EmployeeInfoException(List<String> errorList) {
		super("{\"" + ApplicationConstants.JSON_MESSAGE + "\": \"" + String.join(",", errorList) + "\"}");
	}
}
