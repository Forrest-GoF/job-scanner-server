package com.forrestgof.jobscanner.jobposting.controller.dto;

import com.forrestgof.jobscanner.company.domain.Company;

import lombok.Data;

@Data
public class CompanyDto {
	String name;
	int employees;
	int averageSalary;
	String registrationDate;
	String thumbnailUrl;
	String companyLocation;

	public CompanyDto(Company company) {
		name = company.getName();
		employees = company.getEmployees();
		averageSalary = company.getAverageSalary();
		if (company.getRegistrationDate() != null)
			registrationDate = company.getRegistrationDate().toString();
		thumbnailUrl = company.getThumbnailUrl();
		companyLocation = company.getAddress();
	}
}
