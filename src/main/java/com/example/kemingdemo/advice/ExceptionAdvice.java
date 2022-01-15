package com.example.kemingdemo.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.kemingdemo.exception.EmployeeInfoException;

@ControllerAdvice
public class ExceptionAdvice {

	@ResponseBody
	@ExceptionHandler(EmployeeInfoException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String employeeNotFoundHandler(EmployeeInfoException ex) {
		return ex.getMessage();
	}
}
