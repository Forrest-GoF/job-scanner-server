package com.forrestgof.jobscanner.jobposting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Getter
@Table(name = "stack")
public class TechStack {

	@Id
	@GeneratedValue
	@Column(name = "stack_id")
	private Long id;

	@Column(name = "stack_name")
	private String name;
}
