package com.forrestgof.jobscanner.company.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forrestgof.jobscanner.company.domain.Company;
import com.forrestgof.jobscanner.company.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCompanyService implements CompanyService {

	private final CompanyRepository companyRepository;

	@Override
	@Transactional
	public Company save(Company company) {
		return companyRepository.save(company);
	}

	@Override
	public Company findOne(Long id) {
		return companyRepository.findById(id).orElseThrow();
	}

	@Override
	public boolean existsByName(String name) {
		return companyRepository.existsByName(name);
	}

	@Override
	public Company findByName(String name) {
		return companyRepository.findByName(name).orElseThrow();
	}
}
