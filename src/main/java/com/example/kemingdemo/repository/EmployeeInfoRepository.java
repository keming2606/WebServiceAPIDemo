package com.example.kemingdemo.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.kemingdemo.model.EmployeeInfo;

public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, String> {
	@Query("SELECT e FROM EmployeeInfo e WHERE e.login = :login and e.id != :id")
	EmployeeInfo findByLogin(@Param("login") String login, @Param("id") String id);

	@Query("Select e FROM EmployeeInfo e WHERE e.salary>= :minSalary and e.salary<= :maxSalary ")
	List<EmployeeInfo> retrieveAllWithFilter(@Param("minSalary") BigDecimal minSalary,
			@Param("maxSalary") BigDecimal maxSalary, Pageable pageable);
}
