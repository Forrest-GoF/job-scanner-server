package com.forrestgof.jobscanner.jobposting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.forrestgof.jobscanner.common.entity.BaseTimeEntity;
import com.forrestgof.jobscanner.member.domain.Member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkJob extends BaseTimeEntity {

	@Id
	@GeneratedValue
	@Column(name = "bookmark_job_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_id")
	private JobPosting jobPosting;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private boolean activated;

	public static BookmarkJob of(JobPosting jobPosting, Member member) {
		BookmarkJob bookmarkJob = new BookmarkJob();

		bookmarkJob.jobPosting = jobPosting;
		bookmarkJob.member = member;

		return bookmarkJob;
	}

	public void update(boolean activated) {
		this.activated = activated;
	}
}
