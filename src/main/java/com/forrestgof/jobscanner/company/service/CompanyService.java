package com.forrestgof.jobscanner.company.service;

import java.util.Optional;

import com.forrestgof.jobscanner.company.domain.Company;

public interface CompanyService {

	Company createFrom(String searchName, String thumbnail);

	Optional<Company> findByRawName(String rawName);

	Optional<Company> getCompanyFromNps(String companyName);

	boolean existsByRawName(String googleName);
}
