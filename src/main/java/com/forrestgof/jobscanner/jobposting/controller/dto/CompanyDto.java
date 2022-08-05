package com.forrestgof.jobscanner.jobposting.controller.dto;

import java.time.LocalDateTime;

import com.forrestgof.jobscanner.jobposting.domain.Company;

import lombok.Data;

@Data
public class CompanyDto {
	String name;
	int employees;
	int averageSalary;
	LocalDateTime registrationDate;
	String thumbnailUrl;
	String companyLocation;

	public CompanyDto(Company company) {
		name = company.getName();
		employees = company.getEmployees();
		averageSalary = company.getAverageSalary();
		registrationDate = company.getRegistrationDate();
		thumbnailUrl = company.getThumbnailUrl();
		companyLocation = company.getCompanyLocation();
	}
}
