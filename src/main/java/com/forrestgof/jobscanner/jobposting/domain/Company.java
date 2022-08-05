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
import lombok.Setter;

@Entity
@Getter
@Builder
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
}
