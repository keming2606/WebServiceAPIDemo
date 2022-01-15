package com.example.kemingdemo.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.CollectionUtils;

import com.example.kemingdemo.model.EmployeeInfo;
import com.example.kemingdemo.utils.ApplicationConstants;

public class EmployeeInfoControllerTest extends AbstractMVCTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void addEmployeeInfoByFile() throws Exception {
		String uri = "/users/upload";
		MockMultipartFile file = new MockMultipartFile("file", "test.csv", MediaType.TEXT_PLAIN_VALUE,
				"id,login,name,salary,startDate\r\ne0002,rwesley,Ron Weasley,19234.50,2001-11-16".getBytes());

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.multipart(uri).file(file).contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertTrue(content.contains(ApplicationConstants.SUCCESS_CREATE));
	}

	@Test
	public void retrieveByFilter() throws Exception {
		String uri = "/users";
		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.get(uri).param("minSalary", "1000").param("maxSalary", "3000")
						.param("offset", "1").param("limit", "1").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Map<String, List<EmployeeInfo>> info = super.mapFromJson(content, Map.class);

		assertFalse(CollectionUtils.isEmpty(info.get("result")));

	}

	@Test
	public void createEmployeeInfo() throws Exception {
		EmployeeInfo info = new EmployeeInfo();
		info.setId("emp0001");
		info.setName("Harry Potter");
		info.setLogin("hpotter");
		info.setSalary(BigDecimal.valueOf(1234.00));
		Date date = new SimpleDateFormat("yyyy-mm-dd").parse("2001-11-16");
		info.setStartDate(date);

		String uri = "/users";
		String inputJson = super.mapToJson(info);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertTrue(content.contains(ApplicationConstants.SUCCESS_CREATE));
	}

	@Test
	public void retrieveEmployeeInfo() throws Exception {
		String uri = "/users/emp0002";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		EmployeeInfo info = super.mapFromJson(content, EmployeeInfo.class);

		assertNotNull(info);
		assertEquals(info.getName(), "Harry Potter2");
		assertEquals(info.getLogin(), "hpotter2");
		assertTrue(info.getSalary().compareTo(BigDecimal.valueOf(2234.00)) == 0);
		assertEquals(info.getStartDate(), new SimpleDateFormat("yyyy-MM-dd").parse("2002-11-16"));
	}

	@Test
	public void updateEmployeeInfo() throws Exception {
		String uri = "/users/emp0003";
		EmployeeInfo info = new EmployeeInfo();
		info.setId("emp0003");
		info.setName("Harry Potter3");
		info.setLogin("hpotter3");
		info.setSalary(BigDecimal.valueOf(2234.00));
		Date date = new SimpleDateFormat("yyyy-mm-dd").parse("2003-11-16");
		info.setStartDate(date);
		String inputJson = super.mapToJson(info);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertTrue(content.contains(ApplicationConstants.SUCCESS_UPDATE));
	}

	@Test
	public void deleteEmployeeInfo() throws Exception {
		String uri = "/users/emp0004";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertTrue(content.contains(ApplicationConstants.SUCCESS_DELETE));
	}
}
