package com.forrestgof.jobscanner.company.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.company.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	Optional<Company> findByName(String name);

	boolean existsByName(String name);
}
