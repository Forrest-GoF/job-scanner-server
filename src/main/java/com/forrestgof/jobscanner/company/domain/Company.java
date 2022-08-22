package com.forrestgof.jobscanner.company.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "company_id")
	private Long id;

	@Column(unique = true)
	private String npsSequence;
	private int registrationNumber;	//사업자번호
	private LocalDate updatedAt;	//NPS 데이터 갱신일자

	@Column(unique = true)
	private String key;	//등록이름+사업자번호

	@Column(unique = true)
	private String googleName;
	private String thumbnailUrl;

	@Column(name = "company_name", unique = true)
	private String name;

	private LocalDate foundingDate;
	private int employeeCount;

	private long paidPension;	//국민연금 납입액
	private long averageAnnualSalary;

	private String address;
	private int countyCode;
	private int cityCode;
	private int townCode;

	@Enumerated(value = EnumType.STRING)
	private RegistrationType registrationType;

	@Enumerated(value = EnumType.STRING)
	private RegistrationStatus registrationStatus;

	@Builder
	public Company(String npsSequence, int registrationNumber, LocalDate updatedAt, String key, String googleName,
		String thumbnailUrl, String name, LocalDate foundingDate, int employeeCount, long paidPension,
		long averageAnnualSalary, String address, int countyCode, int cityCode, int townCode,
		RegistrationType registrationType, RegistrationStatus registrationStatus) {
		this.npsSequence = npsSequence;
		this.registrationNumber = registrationNumber;
		this.updatedAt = updatedAt;
		this.key = key;
		this.googleName = googleName;
		this.thumbnailUrl = thumbnailUrl;
		this.name = name;
		this.foundingDate = foundingDate;
		this.employeeCount = employeeCount;
		this.paidPension = paidPension;
		this.averageAnnualSalary = averageAnnualSalary;
		this.address = address;
		this.countyCode = countyCode;
		this.cityCode = cityCode;
		this.townCode = townCode;
		this.registrationType = registrationType;
		this.registrationStatus = registrationStatus;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public void setGoogleName(String googleName) {
		this.googleName = googleName;
	}
}
