package com.example.kemingdemo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EmployeeInfo {

	public EmployeeInfo() {
	}

	public EmployeeInfo(String id, String login, String name, BigDecimal salary, Date startDate) {
		this.id = id;
		this.login = login;
		this.name = name;
		this.salary = salary;
		this.startDate = startDate;
	}

	@Id
	private String id;
	private String login;
	private String name;
	private BigDecimal salary;
	private Date startDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, login, name, salary, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeInfo other = (EmployeeInfo) obj;
		return Objects.equals(id, other.id) && Objects.equals(login, other.login) && Objects.equals(name, other.name)
				&& Objects.equals(salary, other.salary) && Objects.equals(startDate, other.startDate);
	}

	@Override
	public String toString() {
		return "EmployeeInfo [id=" + id + ", login=" + login + ", name=" + name + ", salary=" + salary + ", startDate="
				+ startDate + "]";
	}

}
