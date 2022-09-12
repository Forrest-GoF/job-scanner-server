package com.forrestgof.jobscanner.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.member.domain.ObjectiveCompany;

public interface ObjectiveCompanyRepository extends JpaRepository<ObjectiveCompany, Long> {
}
