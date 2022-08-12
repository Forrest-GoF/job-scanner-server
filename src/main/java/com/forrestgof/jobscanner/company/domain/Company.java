package com.forrestgof.jobscanner.company.domain;

import java.time.LocalDate;

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

	@Column(unique = true)
	private String npsKey;

	@Column(name = "company_name", unique = true)
	private String name;

	private int employees;
	private int averageSalary;
	private LocalDate registrationDate;
	private String thumbnailUrl;
	private String address;

	@Builder
	public Company(String npsKey, String name, int employees, int averageSalary,
		LocalDate registrationDate, String thumbnailUrl, String address) {
		this.npsKey = npsKey;
		this.name = name;
		this.employees = employees;
		this.averageSalary = averageSalary;
		this.registrationDate = registrationDate;
		this.thumbnailUrl = thumbnailUrl;
		this.address = address;
	}
}
