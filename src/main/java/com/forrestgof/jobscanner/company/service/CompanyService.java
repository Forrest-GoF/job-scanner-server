package com.forrestgof.jobscanner.company.service;

import com.forrestgof.jobscanner.company.domain.Company;

public interface CompanyService {

	Company save(Company company);

	Company findOne(Long id);

	Company findByName(String name);

	boolean existsByName(String name);
}
