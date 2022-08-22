package com.forrestgof.jobscanner.company.util.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import com.forrestgof.jobscanner.company.domain.Company;
import com.forrestgof.jobscanner.company.domain.RegistrationStatus;
import com.forrestgof.jobscanner.company.domain.RegistrationType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NpsDetailDto {
	String key;
	String name;
	String registrationNumber;
	String updatedAt;
	String seq;
	String address;
	String registrationStatus;
	String registrationType;
	String countyCode;
	String cityCode;
	String townCode;
	String employeeCount;
	String paidPension;
	String pensionPerEmployee;
	String foundingDate;
	String averageAnnualSalary;

	public Company toCompany() {
		DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter monthFormatter = new DateTimeFormatterBuilder()
			.appendPattern("yyyyMM")
			.parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
			.toFormatter();

		return Company.builder()
			.uniqueKey(key)
			.name(name)
			.registrationNumber(Integer.parseInt(registrationNumber))
			.updatedAt(LocalDate.parse(updatedAt, monthFormatter))
			.npsSequence(seq)
			.address(address)
			.registrationStatus(RegistrationStatus.valueOf(registrationStatus))
			.registrationType(RegistrationType.valueOf(registrationType))
			.countyCode(Integer.parseInt(countyCode))
			.cityCode(Integer.parseInt(cityCode))
			.townCode(Integer.parseInt(townCode))
			.employeeCount(Integer.parseInt(employeeCount))
			.paidPension(Long.parseLong(paidPension))
			.foundingDate(LocalDate.parse(foundingDate, dayFormatter))
			.averageAnnualSalary(Long.parseLong(averageAnnualSalary))
			.build();
	}
}
