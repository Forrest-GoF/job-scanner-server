package com.forrestgof.jobscanner.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjectiveCompany extends BaseTimeEntity {

	@Id
	@Column(name = "member_id")
	private Long id;

	@OneToOne
	@MapsId
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "objective_employee_count")
	private int employeeCount;

	@Column(name = "objective_salary")
	private long salary;
}
