package com.forrestgof.jobscanner.duty.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Duty extends BaseTimeEntity  {

	@Id
	@GeneratedValue
	@Column(name = "duty_id")
	private Long id;

	@Column(name = "duty_name", unique = true)
	private String name;
}
