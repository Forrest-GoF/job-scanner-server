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
public class Tag {

	@Id
	@GeneratedValue
	@Column(name = "tag_id")
	private Long id;

	@Column(name = "tag_name", unique = true)
	private String name;

	public Tag(String name) {
		this.name = name;
	}
}
