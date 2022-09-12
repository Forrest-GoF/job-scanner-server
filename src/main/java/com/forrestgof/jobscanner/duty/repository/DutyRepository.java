package com.forrestgof.jobscanner.duty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.forrestgof.jobscanner.duty.domain.Duty;

public interface DutyRepository extends JpaRepository<Duty, Long> {
}
