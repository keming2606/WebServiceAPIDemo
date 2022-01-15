package com.example.kemingdemo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.kemingdemo.model.EmployeeInfo;

@Component
public class CSVParserHelper {

	public boolean hasCSVFormat(MultipartFile file) {
		if (!ApplicationConstants.FILE_TYPE_CSV.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public List<EmployeeInfo> csvToEmployeeInfoList(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

			List<EmployeeInfo> objectList = new ArrayList<EmployeeInfo>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();

			for (CSVRecord csvRecord : csvRecords) {
				EmployeeInfo object = new EmployeeInfo();

				object.setId(csvRecord.get("id"));

				object.setLogin(csvRecord.get("login"));

				object.setName(csvRecord.get("name"));

				Double salary = Double.valueOf(csvRecord.get("salary"));
				object.setSalary(BigDecimal.valueOf(salary));

				String dateStr = csvRecord.get("startDate");
				Date date = null;
				try {
					if (dateStr.length() == 10) {

						date = new SimpleDateFormat("yyyy-mm-dd").parse(dateStr);

					} else if (dateStr.length() == 9) {
						date = new SimpleDateFormat("dd-MMM-yy").parse(dateStr.toLowerCase());
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				object.setStartDate(date);
				objectList.add(object);
			}

			return objectList;
		} catch (IOException e) {
			throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
		}
	}

}