package com.example.kemingdemo.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.kemingdemo.exception.EmployeeInfoException;
import com.example.kemingdemo.model.EmployeeInfo;
import com.example.kemingdemo.repository.EmployeeInfoRepository;
import com.example.kemingdemo.repository.OffsetBasedPageRequest;
import com.example.kemingdemo.utils.ApplicationConstants;
import com.example.kemingdemo.utils.CSVParserHelper;

@Service
public class EmployeeInfoService {
	@Autowired
	private EmployeeInfoRepository repository;

	@Autowired
	private CSVParserHelper parser;

	public String processFile(MultipartFile file) {
		try (InputStream in = file.getInputStream()) {
			List<EmployeeInfo> infoList = parser.csvToEmployeeInfoList(in);
			List<EmployeeInfo> processInfoList = new ArrayList<EmployeeInfo>();
			List<String> idList = new ArrayList<String>();
			List<String> errorList = new ArrayList<String>();
			int row = 2;// excluding header
			for (EmployeeInfo info : infoList) {
				String id = info.getId();
				if (id.isBlank()) {
					errorList.add(ApplicationConstants.INVALID_ID + " on row " + row);
				} else if (!id.startsWith("#")) {

					if (idList.contains(id)) {
						errorList.add(ApplicationConstants.DUPLICATED_ID + " on row " + row);
					} else {
						idList.add(id);
						String error = validateEmployeeInfo(info);
						if (!error.isBlank()) {
							errorList.add(error + " on row " + row);
						} else {
							processInfoList.add(info);
						}
					}
				}
				row++;
			}
			if (CollectionUtils.isEmpty(errorList)) {
				repository.saveAll(infoList);
				return ApplicationConstants.SUCCESS_CREATE;
			} else {
				throw new EmployeeInfoException(errorList);
			}
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

	public String addNewEmployeeInfo(EmployeeInfo info) {
		String error = validateNewEmployeeInfo(info);
		if (!error.isBlank()) {
			throw new EmployeeInfoException(error);

		} else {
			repository.save(info);
			return ApplicationConstants.SUCCESS_CREATE;
		}
	}

	public Optional<EmployeeInfo> retrieveById(String id) {
		return repository.findById(id);
	}

	public String updateEmployeeInfo(EmployeeInfo info, String id) {
		return repository.findById(id).map(employeeInfo -> {
			String error = validateEmployeeInfo(info);
			if (!error.isBlank()) {
				throw new EmployeeInfoException(error);
			} else {
				employeeInfo.setName(info.getName());
				employeeInfo.setLogin(info.getLogin());
				employeeInfo.setSalary(info.getSalary());
				employeeInfo.setStartDate(info.getStartDate());
				repository.save(info);
				return ApplicationConstants.SUCCESS_UPDATE;
			}
		}).orElseThrow(() -> new EmployeeInfoException(ApplicationConstants.ID_NOT_FOUND));
	}

	public String deleteEmployeeInfo(String id) {
		if (repository.findById(id).isPresent()) {
			repository.deleteById(id);
			return ApplicationConstants.SUCCESS_DELETE;
		} else {
			throw new EmployeeInfoException(ApplicationConstants.ID_NOT_FOUND);
		}

	}

	public List<EmployeeInfo> findAllWithFilter(BigDecimal minSalary, BigDecimal maxSalary, int offset, int limit) {
		if (limit == 0) {
			limit = Integer.MAX_VALUE;// no limit
		}
		Pageable pageable = new OffsetBasedPageRequest(limit, offset);
		return repository.retrieveAllWithFilter(minSalary, maxSalary, pageable);
	}

	private String validateNewEmployeeInfo(EmployeeInfo info) {
		String id = info.getId();
		if (id.isBlank()) {
			return ApplicationConstants.INVALID_ID;
		}
		if (repository.findById(id).isPresent()) {
			return ApplicationConstants.ID_EXISTS;
		}
		return validateEmployeeInfo(info);
	}

	private String validateEmployeeInfo(EmployeeInfo info) {

		String login = info.getLogin();
		if (login.isBlank()) {
			return ApplicationConstants.INVALID_LOGIN;
		}
		if (repository.findByLogin(login, info.getId()) != null) {
			return ApplicationConstants.LOGIN_NOT_UNIQUE;
		}
		BigDecimal salary = info.getSalary();
		if (salary.doubleValue() < 0) {
			return ApplicationConstants.INVALID_SALARY;
		}
		Date startDate = info.getStartDate();
		if (startDate == null) {
			return ApplicationConstants.INVALID_DATE;
		}
		return "";
	}
}
