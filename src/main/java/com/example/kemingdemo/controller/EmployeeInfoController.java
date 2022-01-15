package com.example.kemingdemo.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.kemingdemo.exception.EmployeeInfoException;
import com.example.kemingdemo.model.EmployeeInfo;
import com.example.kemingdemo.service.EmployeeInfoService;
import com.example.kemingdemo.utils.ApplicationConstants;

@RestController
public class EmployeeInfoController {

	@Autowired
	private EmployeeInfoService service;

	@PostMapping(path = "/users/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	Map<String, String> addEmployeeInfoByFile(@RequestParam("file") MultipartFile file) {
		return Collections.singletonMap(ApplicationConstants.JSON_MESSAGE, service.processFile(file));
	}

	@GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, List<EmployeeInfo>> retrieveByFilter(@RequestParam String minSalary, @RequestParam String maxSalary,
			@RequestParam int offset, @RequestParam int limit) {
		BigDecimal minSalaryDec = BigDecimal.valueOf(0);
		if (!minSalary.isBlank())
			minSalaryDec = BigDecimal.valueOf(Double.valueOf(minSalary));
		BigDecimal maxSalaryDec = BigDecimal.valueOf(4000);
		if (!maxSalary.isBlank())
			maxSalaryDec = BigDecimal.valueOf(Double.valueOf(maxSalary));

		return Collections.singletonMap("result", service.findAllWithFilter(minSalaryDec, maxSalaryDec, offset, limit));
	}

	@PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	Map<String, String> addEmployeeInfo(@RequestBody EmployeeInfo info) {

		return Collections.singletonMap(ApplicationConstants.JSON_MESSAGE, service.addNewEmployeeInfo(info));
	}

	@GetMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	EmployeeInfo retrieveById(@PathVariable String id) {

		return service.retrieveById(id).orElseThrow(() -> new EmployeeInfoException(ApplicationConstants.ID_NOT_FOUND));
	}

	@PutMapping(path = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, String> updateEmployeeInfo(@RequestBody EmployeeInfo info, @PathVariable String id) {

		return Collections.singletonMap(ApplicationConstants.JSON_MESSAGE, service.updateEmployeeInfo(info, id));
	}

	@DeleteMapping(path = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, String> deleteEmployeeInfo(@PathVariable String id) {

		return Collections.singletonMap(ApplicationConstants.JSON_MESSAGE, service.deleteEmployeeInfo(id));

	}

}
