package com.forrestgof.jobscanner.jobposting.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(JobLike.JobLikeId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobLike extends BaseTimeEntity {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id")
	private JobPosting jobPosting;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Data
	public static class JobLikeId implements Serializable {
		private JobPosting jobPosting;
		private Member member;
	}
}
