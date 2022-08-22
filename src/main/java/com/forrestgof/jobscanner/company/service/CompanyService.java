package com.forrestgof.jobscanner.company.service;

import java.util.Optional;

import com.forrestgof.jobscanner.company.domain.Company;
import com.forrestgof.jobscanner.jobposting.util.dto.GoogleJobDto;

public interface CompanyService {

	Long createFromGoogleJob(GoogleJobDto googleJobDto);

	Company findOne(Long id);

	Company findByGoogleName(String googleName);

	Optional<Company> getCompanyFromNps(String companyName);

	boolean existsByGoogleName(String googleName);
}
