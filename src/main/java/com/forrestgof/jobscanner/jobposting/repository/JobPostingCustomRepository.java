package com.forrestgof.jobscanner.jobposting.repository;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.domain.JobType;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JobPostingCustomRepository {

	private final EntityManager em;

	public List<JobPosting> findFilterJobs(Map<String, String> params, List<String> tags) {
		int page = Integer.parseInt(params.getOrDefault("page", "1"));
		int size = Integer.parseInt(params.getOrDefault("size", "20"));
		int employees = Integer.parseInt(params.getOrDefault("minEmployees", "0"));
		long salary = Long.parseLong(params.getOrDefault("minSalary", "0"))*10000;
		JobType jobType = JobType.valueOf(params.getOrDefault("type", "FULLTIME"));

		return em.createQuery("select j from JobPosting j" +
				" join fetch j.company c" +
				" where c.employeeCount >= :employees" +
				" and j.type = :jobType" +
				" and c.averageAnnualSalary >= :salary", JobPosting.class)
			.setParameter("employees", employees)
			.setParameter("salary", salary)
			.setParameter("jobType", jobType)
			.setFirstResult(page)
			.setMaxResults(size)
			.getResultList();
	}
}
