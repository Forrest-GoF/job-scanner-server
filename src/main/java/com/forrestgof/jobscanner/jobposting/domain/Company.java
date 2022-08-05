package com.forrestgof.jobscanner.jobposting.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {

	@Id
	@GeneratedValue
	@Column(name = "company_id")
	private Long id;

	private int registrationNum;

	@Column(name = "company_name")
	private String name;

	private int employees;
	private int averageSalary;
	private LocalDateTime registrationDate;
	private String thumbnailUrl;
	private String companyLocation;

	@Builder
	public Company(int registrationNum, String name, int employees, int averageSalary,
		LocalDateTime registrationDate, String thumbnailUrl, String companyLocation) {
		this.registrationNum = registrationNum;
		this.name = name;
		this.employees = employees;
		this.averageSalary = averageSalary;
		this.registrationDate = registrationDate;
		this.thumbnailUrl = thumbnailUrl;
		this.companyLocation = companyLocation;
	}
}
