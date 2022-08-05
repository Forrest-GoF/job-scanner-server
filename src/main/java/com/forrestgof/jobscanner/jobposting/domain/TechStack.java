package com.forrestgof.jobscanner.jobposting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "stack")
public class TechStack {

	@Id
	@GeneratedValue
	@Column(name = "stack_id")
	private Long id;

	@Column(name = "stack_name")
	private String name;

	public TechStack(String name) {
		this.name = name;
	}
}
