package com.forrestgof.jobscanner.jobposting.controller.dto;

import com.forrestgof.jobscanner.company.domain.Company;

import lombok.Data;

@Data
public class CompanyDto {
	String name;
	int employees;
	String averageSalary;
	String registrationDate;
	String thumbnailUrl;
	String companyLocation;

	public CompanyDto(Company company) {
		name = company.getName();
		employees = company.getEmployeeCount();
		if (company.getAverageAnnualSalary() > 0)
			averageSalary = String.valueOf(company.getAverageAnnualSalary());
		if (company.getFoundingDate() != null)
			registrationDate = company.getFoundingDate().toString();
		thumbnailUrl = company.getThumbnailUrl();
		companyLocation = company.getAddress();
	}
}
