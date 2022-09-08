package com.forrestgof.jobscanner.jobposting.repository;

import static com.forrestgof.jobscanner.company.domain.QCompany.*;
import static com.forrestgof.jobscanner.jobposting.domain.QJobPosting.*;
import static com.forrestgof.jobscanner.jobposting.domain.QJobTag.*;
import static com.forrestgof.jobscanner.jobposting.domain.QTag.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import com.forrestgof.jobscanner.jobposting.controller.dto.JobSearchCondition;
import com.forrestgof.jobscanner.jobposting.controller.dto.SortingCondition;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.jobposting.domain.JobType;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class JobPostingRepositoryImpl implements JobPostingRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public JobPostingRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<JobPosting> findFilterJobs(JobSearchCondition condition) {
		JPAQuery<JobPosting> query = createQueryByCondition(condition);

		Optional<SortingCondition> optionalSortingCondition = condition.getSortingCondition();
		if (optionalSortingCondition.isPresent())
			query = orderBy(query, optionalSortingCondition.get());

		return query.fetch();
	}

	private JPAQuery<JobPosting> createQueryByCondition(JobSearchCondition condition) {
		return queryFactory
			.selectFrom(jobPosting)
			.join(jobPosting.company, company)
			.leftJoin(jobPosting.jobTags, jobTag)
			.leftJoin(jobTag.tag, tag)
			.where(
				notExpired(),
				employeesGoe(condition.getMinEmployees()),
				salaryGoe(condition.getSalaryUnit()),
				tagIn(condition.getTags()),
				jobTypeIn(condition.getJobType()))
			.distinct()
			.offset(condition.getPage())
			.limit(condition.getSize());
	}

	private BooleanExpression notExpired() {
		return jobPosting.isExpired.not();
	}

	private BooleanExpression employeesGoe(long employees) {
		return company.employeeCount.goe(employees);
	}

	private BooleanExpression salaryGoe(long salary) {
		return company.averageAnnualSalary.goe(salary);
	}

	private BooleanExpression tagIn(List<String> tags) {
		if (tags==null) {
			return null;
		}
		return tag.name.in(tags);
	}

	private BooleanExpression jobTypeIn(List<JobType> jobType) {
		if (jobType==null) {
			return null;
		}
		return jobPosting.type.in(jobType);
	}

	private JPAQuery<JobPosting> orderBy(JPAQuery<JobPosting> query,
		SortingCondition sortingCondition) {
		return switch (sortingCondition) {
			case POSTED ->
				query.orderBy(
					new CaseBuilder()
						.when(jobPosting.postedAt.isNull())
						.then(LocalDate.EPOCH)
						.otherwise(jobPosting.postedAt)
						.desc());

			case EXPIRED ->
				query.orderBy(
					new CaseBuilder()
						.when(jobPosting.expiredAt.isNull())
						.then(LocalDate.EPOCH)
						.otherwise(jobPosting.expiredAt)
						.desc());
		};
	}
}
