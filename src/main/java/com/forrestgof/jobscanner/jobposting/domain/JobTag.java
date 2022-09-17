package com.forrestgof.jobscanner.jobposting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;
import com.forrestgof.jobscanner.jobposting.domain.JobPosting;
import com.forrestgof.jobscanner.tag.domain.Tag;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobTag extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "job_tag_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id")
	private JobPosting jobPosting;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id")
	private Tag tag;

	public JobTag(JobPosting jobPosting, Tag tag) {
		this.jobPosting = jobPosting;
		this.tag = tag;
	}
}
